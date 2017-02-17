
package Rtp;

import java.io.File;
import java.io.IOException;
import javax.media.DataSink;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoProcessorException;
import javax.media.NotRealizedError;
import javax.media.Processor;
import javax.media.control.FormatControl;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;

public class RTPTransmitter
{

/**
 * @param args
 */
public static void main(String[] args)
{
    
    File f = new File("F:\\nirmal\\1.wav");
    Format format;
    format = new AudioFormat(AudioFormat.ULAW_RTP, 8000, 8, 1);
    Processor processor = null;
    
    try
    {
        processor = Manager.createProcessor(f.toURI().toURL());
    } catch (Exception e)
    {
        e.printStackTrace();
        System.exit(-1);
    } 
    

    // configure the processor
    processor.configure();

    while (processor.getState() != Processor.Configured)
    {
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   

    processor.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW_RTP));

    TrackControl track[] = processor.getTrackControls();

    boolean encodingOk = false;
    System.out.println("tracks : "+track.length);
    // Go through the tracks and try to program one of them to
    // output gsm data.

    for (int i = 0; i < track.length; i++)
    {
        if (!encodingOk && track[i] instanceof FormatControl)
        {
            if (((FormatControl) track[i]).setFormat(format) == null)
            {

                track[i].setEnabled(false);
            } else
            {
                encodingOk = true;
            }
        } else
        {
            // we could not set this track to gsm, so disable it
            track[i].setEnabled(false);
        }
    }

    //realize the processor
    processor.realize();
    while(processor.getState() != processor.Realized){
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // At this point, we have determined where we can send out
    // gsm data or not.
    if (encodingOk)
    {       
        // get the output datasource of the processor and exit
        // if we fail
        DataSource ds = null;

        try
        {
            ds = processor.getDataOutput();
        } catch (NotRealizedError e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
        // hand this datasource to manager for creating an RTP
        // datasink our RTP datasink will multicast the audio
        try
        {
            String url = "rtp://192.168.43.121:8000/audio/1";

            MediaLocator m = new MediaLocator(url);

            DataSink d = Manager.createDataSink(ds, m);
            d.open();
            d.start();

            System.out.println("Starting processor");
            processor.start();
           // System.out.println(Processor.getMediaTime());
            System.out.println("Processor Started");
            Thread.sleep(30000);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

}

}
