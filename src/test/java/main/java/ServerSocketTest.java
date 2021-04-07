package main.java;

import main.java.server.Game;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import static java.lang.Thread.sleep;

public class ServerSocketTest {
    @Test
    public void testMove() throws InterruptedException, IOException {
//        InputStream inputStream = new StringBufferInputStream("");
//        OutputStream outputStream = Mockito.mock(OutputStream.class);
//        Mockito.when(inputStream.read())
//                .thenReturn("")
//                .thenReturn("");
        System.out.println("entered");
        var spy = new ServerSocketWrapperSpy("" +
                "\nArrowUp,ArrowLeft");
//
        var pool = Executors.newFixedThreadPool(50);
        Phaser barrier = new Phaser(0);
//
        Game game = new Game(50, barrier);
        game.addPlayer(spy).run();
//        sleep(5000);
        System.out.println(spy.getOutput());

//        pool.execute(game.new Player(spy, 'X'));
//        sleep(1000);
//        pool.execute(game.new Player(spy, 'O'));
//        sleep(1000);
//        System.out.println(spy.getOutputStream());
//        System.out.println("Helloworld");
    }
}
