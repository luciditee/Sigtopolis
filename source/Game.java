
import com.sigmatauproductions.isomatrix.game.IsomatrixBootstrap;
import com.sigmatauproductions.sigtopolis.game.Sigtopolis;
import org.newdawn.slick.SlickException;

public final class Game {
    
    public static void main(String[] args) throws SlickException {
        IsomatrixBootstrap.start(new Sigtopolis());
    }
    
}