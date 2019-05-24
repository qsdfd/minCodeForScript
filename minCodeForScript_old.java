import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.RandomBehaviourHook;
import org.osbot.rs07.script.RandomEvent;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@ScriptManifest(name = "", author = "dokato", version = 1.0, info = "", logo = "") 
public class MainTestBot extends Script {
	
	private static final Color standardTxtColor =new Color(255, 255, 255);
	private static final Color breakRectColor = new Color(0, 0, 0, 175);
	
	private static final Rectangle breakRect = new Rectangle(190, 110, 390, 50);
	
	private boolean startb = true;

	private long timeBegan;
	private long timeRan;
	private long timeReset;
	private long timeSinceReset;
	private long timeBotted;
	private long timeOffline;
	
	private String status;
	
	private long timeLastBreaked;
	private long timeSinceLastBreaked;
	private long timeBreakStart;
	
	private static final long milisecondsPerMinute = 60000; 
	private long bottingTime = 52 * milisecondsPerMinute;
	private long breakingTime = 14 * milisecondsPerMinute;
	private static final long randomizeValue = 5 * milisecondsPerMinute;
	
	private long timeBotting;
	private long timeBreaing;
	
	private boolean resetBreakCheck = true;
	private boolean hasStarted = false;
	
	@Override
    public void onStart(){
		resetTime();
		
		mouseListenerStuff();
		randomEventStuff();
    }
	
    public int onLoop() throws InterruptedException{
    	status="loop started";
    	if(!needToBreak()){
	    	if(getClient().isLoggedIn()){
	    		breakTimeProcedures();
				
	    	}
    	}else{
			doBreak();
		}
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
    	this.timeSinceLastBreaked = System.currentTimeMillis() - this.timeLastBreaked;
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
		if(hasStarted)
			g.drawString("Last break: " + ft(this.timeSinceLastBreaked), x, getY(startY, value+=increment));
		g.drawString("Status: " + status, x, getY(startY, value+=increment));
		
		if(hasStarted && needToBreak()){
			g.setColor(breakRectColor);
			fillRect(g, breakRect);
			g.setColor(standardTxtColor);
			g.drawString("Have to break for: " + ft(this.timeBreaing) , 275, 130);
			g.drawString("Have been breaking for: " + ft((System.currentTimeMillis() - this.timeBreakStart)), 275, 145);
		}
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
    
	 private boolean needToBreak(){
			status="returning if i need to break";
			return (timeSinceLastBreaked > this.timeBotting) && (timeSinceLastBreaked < (this.timeBotting + this.timeBreaing));
		}
		
	    private void doBreak() throws InterruptedException{
			status="Have to break";
			if(getClient().isLoggedIn()){
				resetBreakCheck=true;
				status="logging out to break";
				getLogoutTab().logOut();
				sleep(random(1000,1600));
				this.timeBreakStart = System.currentTimeMillis();
			}
		}
		
		private void breakTimeProcedures(){
			status="break time procedures";
			if(resetBreakCheck){
				resetBreakCheck=false;
				this.timeLastBreaked = System.currentTimeMillis();
				
				this.timeBotting = getBottingTime();
				this.timeBreaing = getBreakingTime();
				
				log("After " + ft(this.timeBotting) + " gonna break for " + ft(this.timeBreaing));
			}
			this.hasStarted = true;
		}
		
		private long getBottingTime(){
			status="getting bottingTime";
			return this.bottingTime + getRandomBreakValue();
		}
		
		private long getBreakingTime(){
			status="getting breakingTime";
			return this.breakingTime + getRandomBreakValue();
		}
		
		private long getRandomBreakValue(){
			status="getting random break value";
			return  ThreadLocalRandom.current().nextLong(-randomizeValue, randomizeValue);
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
    
    private void randomEventStuff(){
    	try {
		    this.bot.getRandomExecutor().registerHook(new RandomBehaviourHook(RandomEvent.AUTO_LOGIN) {
		        @Override
		        public boolean shouldActivate() {
		        	if(hasStarted && needToBreak()){
		        		status="Breaking";
		        		return false;
		        	}else{
		        		status="Loging in";
		        		return super.shouldActivate();
		        	}
		        }
		    });
		} catch (Exception ex) {
		    log("something went wrong");
		}
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