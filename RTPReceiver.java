/*
   OutputStream os = new CountingOutpurStream(socket.getOutputStream());
    InputStream is = new CountingInputStream(socket.getInputStream());
*/
 package Rtp;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;

public class RTPReceiver
{

/**
 * @param args
 */
public static void main(String[] args)
{
    String url = "rtp://192.168.43.121:8000/audio/1";
    MediaLocator mrl = new MediaLocator(url);

    // Create a player for this rtp session
    Player player = null;
    try
    {
        player = Manager.createPlayer(mrl);
    } catch (NoPlayerException e)
    {
        e.printStackTrace();
        System.exit(-1);
    } catch (MalformedURLException e)
    {
        e.printStackTrace();
        System.exit(-1);
    } catch (IOException e)
    {
        e.printStackTrace();
        System.exit(-1);
    }

    if (player != null)
    {
        System.out.println("Player created.");
        player.realize();
       
        while (player.getState() != Player.Realized)
        {
            try
            {
                Thread.sleep(10);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Starting player");
        player.start();
    } else
    {
        System.err.println("Player won't create.");
        System.exit(-1);
    }

    System.out.println("Exiting.");
}

}

