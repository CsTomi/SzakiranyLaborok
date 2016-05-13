import javax.swing.*;

public class Updater extends SwingWorker<Double, Object> {

    public Double doInBackground() {
	 	
    	Calculator calc = new Calculator();
    	this.setProgress(0);
		
		try {
			int x = 0;
			while(x < 100) { // Hiba kezelés			
				x = calc.work();
				
				if (isCancelled())
					break;
				
				this.setProgress(x); // fire property changed	
				//this.firePropertyChange("VALUE", null, x);
			}
			
			this.setProgress(100);
			return calc.get();
		
		} catch (InterruptedException e) {
			return 0.0;
		}
    }
}
