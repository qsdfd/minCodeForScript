import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@ScriptManifest(name = "", author = "dokato", version = 1.0, info = "", logo = "") 
public class Main extends Script {
	
	private static final Color standardTxtColor =new Color(255, 255, 255);
	
	private boolean startb = true;

	private long timeBegan;
	private long timeRan;
	private long timeReset;
	private long timeSinceReset;
	private long timeBotted;
	private long timeOffline;
	
	private String status;
	
	
	@Override
    public void onStart(){
		resetTime();
		
		mouseListenerStuff();
    }
	
    public int onLoop() throws InterruptedException{
    	status="loop started";
    	
    	return 0;
    }

    @Override
    public void onPaint(Graphics2D g1){
    	
    	if(this.startb){
    		this.startb=false;
    		this.timeBegan = System.currentTimeMillis();
    		this.timeReset = timeBegan;
    	}
    	this.timeRan = (System.currentTimeMillis() - this.timeBegan);
    	this.timeSinceReset = (System.currentTimeMillis() - this.timeReset);
		if (getClient().isLoggedIn()) {
			this.timeBotted = (this.timeSinceReset - this.timeOffline);
		} else {
			this.timeOffline = (this.timeSinceReset - this.timeBotted);
		}
		
		Graphics2D g = g1;

		int startY = 65;
		int increment = 15;
		int value = (-increment);
		int x = 20;
		
		g.setFont(new Font("Arial", 0, 13));
		g.setColor(standardTxtColor);
		g.drawString("Acc: " + getBot().getUsername().substring(0, getBot().getUsername().indexOf('@')), x,getY(startY, value+=increment));
		g.drawString("World: " + getWorlds().getCurrentWorld(),x,getY(startY, value+=increment));
		value+=increment;
		g.drawString("Version: " + getVersion(), x, getY(startY, value+=increment));
		g.drawString("Runtime: " + ft(this.timeRan), x, getY(startY, value+=increment));
		g.drawString("Time botted: " + ft(this.timeBotted), x, getY(startY, value+=increment));
		g.drawString("Status: " + status, x, getY(startY, value+=increment));
    }
    
    public void onMessage(Message message) throws InterruptedException {
		
	}

	public void onExit() {
		
	}
    
    private int getY(int startY, int value){
		return startY + value;
	}
	
	private void fillRect(Graphics2D g, Rectangle rect){
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
    
	 
	
    private void mouseListenerStuff(){
    	getBot().addMouseListener(new MouseListener() {
			
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    private void resetTime(){
		this.timeReset = System.currentTimeMillis();
		this.timeBotted = 0;
		this.timeOffline = 0;
	}
    
	private String ft(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(duration));
		if (days == 0L) {
			res = hours + ":" + minutes + ":" + seconds;
		} else {
			res = days + ":" + hours + ":" + minutes + ":" + seconds;
		}
		return res;
	}
}