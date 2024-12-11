package logic;

import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class CaptureAudio {

    private TargetDataLine line; 										// Reference to the capture line
	private volatile boolean capturing  = true; 
    private double minObserved = Double.MAX_VALUE; // Minimum value dynamically observed
    private double maxObserved = 70; 							// Max value 
    private double avgDB = 0; 										// Mobile media of decibels
    private double avgDBSmoothed = 0; 						// Second layer of moving media 
    private final double alpha = 0.2; 								// Smoothing factor for the first layer
    private final double beta = 0.1;  								// Smoothing factor for the second layer

	/**
	 * Methods.
	 */

    /**
     * <h1>setMaxObserved()</h1>
     * <p>Sets the maximum observed value in a thread-safe manner.</p>
     *
     * @param maxObserved The new maximum observed value to be set.
     */
    public synchronized void setMaxObserved(double maxObserved) {
        this.maxObserved = maxObserved;
    }
    
    /**
     * <h1>getMaxObserved()</h1>
     * <p>Retrieves the maximum observed value in a thread-safe manner.</p>
     *
     * @return The current maximum observed value.
     */
    public synchronized double getMaxObserved() {
        return maxObserved;
    }
    
    /**
     * <h1>captureAudio()</h1>
     * <p>Captures audio in real-time using the specified audio format and processes the audio levels.</p>
     *
     * @param format The {@link AudioFormat} specifying the audio capture settings (e.g., sample rate, bit depth, channels).
     * @param listener A listener that receives updates about the audio levels in decibels.
     */
    public void captureAudio(AudioFormat format, AudioLevelListener listener) {
    	capturing = true;
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
        	JOptionPane.showMessageDialog(null, "The audio format is not supported: " + format, "Audio capture error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
        	// Assign the audio line to the instance variable
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            System.out.println("Capturando audio con formato: " + format);

            // Buffer to store audio data
            byte[] buffer = new byte[4096];

            while (capturing && !Thread.currentThread().isInterrupted()) {
                int readBytes = line.read(buffer, 0, buffer.length);
                if (readBytes > 0) {
                    double dbLevel = calculateDecibels(buffer, readBytes);
                    listener.onAudioLevel(dbLevel);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error capturing audio: " + e.getMessage(), 
                    "Audio capture error", JOptionPane.ERROR_MESSAGE);
        } finally {
            stopCapturing(); 
        }
    }

    /**
     * <h1>stopCapturing()</h1>
     * <p>Stops the audio capturing process and releases the associated resources.</p>
     */
    public void stopCapturing() {
        capturing  = false; 
        if (line != null && line.isOpen()) {
            line.stop();
            line.close();
        }
    }

    /**
     * <h1>calculateDecibels()</h1>
     * <p>This method processes the audio data in the form of a byte buffer to calculate the RMS (Root Mean Square) value, 
     * which is then converted into a decibel (dB) value. The decibel value is smoothed to reduce sudden fluctuations, 
     * and the result is normalized to a scale between 0 and 100. This scale represents the relative loudness of the 
     * audio signal, considering dynamic range adjustments to prevent it from being too narrow.</p>
     * 
     * @param buffer The byte array containing the audio sample data.
     * @param readBytes The number of bytes read from the audio input, which determines the number of audio samples.
     * 
     * @return A value between 0 and 100 that represents the calculated loudness in decibels, 
     *         with smoothing and dynamic range adjustment applied.
     */
    private double calculateDecibels(byte[] buffer, int readBytes) {
        double leftRMS = 0.0;
        double rightRMS = 0.0;
        int samples = readBytes / 4;
        
        // Audio samples
        // Audio samples -- > Process
        for (int i = 0; i < readBytes; i += 4) {
            int leftChannel = (buffer[i + 1] << 8) | (buffer[i] & 0xFF);
            leftRMS += leftChannel * leftChannel;

            int rightChannel = (buffer[i + 3] << 8) | (buffer[i + 2] & 0xFF);
            rightRMS += rightChannel * rightChannel;
        }

        // Audio samples -- > Calculate average RMS of both channels
        leftRMS = Math.sqrt(leftRMS / samples);
        rightRMS = Math.sqrt(rightRMS / samples);
        double rms = (leftRMS + rightRMS) / 2;

        if (rms == 0) {  // If there is no sound
            return 0; 
        }

        // Calculation of values
        // Calculation of values -- > Convert RMS to decibels
        double db = 20 * Math.log10(rms); 

        // Calculation of values -- > Apply mobile media to smooth the peaks (first layer)
        avgDB = avgDB * (1 - alpha) + db * alpha;

        // Calculation of values -- > Apply second coat of smoothing (cumulative moving media)
        avgDBSmoothed = avgDBSmoothed * (1 - beta) + avgDB * beta;

        // Dynamic range
        // Dynamic range -- >  Update observed minimum value
        minObserved = Math.min(minObserved, avgDBSmoothed);

        // Dynamic range -- > Avoid too narrow a dynamic range
        if (maxObserved - minObserved < 20) {
            maxObserved = minObserved + 20;
        }

        // Map decibels
        // Map decibels -- > Dynamic range [minObserved, maxObserved]
        double scale = (avgDBSmoothed - minObserved) / (maxObserved - minObserved);

        // Map decibels -- > Limit scale to range 0-100
        return Math.max(0, Math.min(scale * 100, 100));
    }   
}

