//package de.school.indiegame;
//
//import com.badlogic.gdx.*;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.Touchable;
//import com.badlogic.gdx.utils.viewport.*;
//
//import static com.badlogic.gdx.Gdx.input;
//import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.actor;
//
//public class Toolbar extends ScreenAdapter {
//
//    public static Stage toolbarStage;
//
//    public void create() {
//        toolbarStage = new Stage(new ExtendViewport(100, 400));
//        //toolbarStage.getViewport().setScreenSize(100,400);
//        input.setInputProcessor(toolbarStage);
//
//        Gdx.input.setInputProcessor(new InputAdapter() {
//            @Override
//            public boolean keyDown(int keycode) {
//                if (keycode == Input.Keys.NUM_1) {
//                    System.out.println("Taste 1 wurde gedrückt");
//                }
//                if (keycode == Input.Keys.NUM_2) {
//                    System.out.println("Taste 2 wurde gedrückt");
//                }
//                if (keycode == Input.Keys.NUM_3) {
//                    System.out.println("Taste 3 wurde gedrückt");
//                }
//                return false;
//            }
//        });
//    }
//
//
//    public void resize (int width, int height) {
//        toolbarStage.getViewport().update(width, height, false);
//    }
//
//    public void render () {
//        float delta = Gdx.graphics.getDeltaTime();
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        toolbarStage.act(delta);
//        toolbarStage.draw();
//    }
//
//
//    public void dispose () {
//        toolbarStage.dispose();
//    }
//
//    /*public Actor hit (float x, float y, boolean touchable) {
//        if (touchable && getTouchable() != Touchable.enabled) return null;
//        return x >= 0 && x < width && y >= 0 && y < height ? this : null;
//    }
//
//    actor.setBounds(0, 0, texture.getWidth(), texture.getHeight());
//
//    actor.addListener(new InputListener() {
//        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//            System.out.println("down");
//            return true;
//        }
//
//        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//            System.out.println("up");
//        }
//    });*/
//}
//
//
//
////switch tools with 1 (Spitzhacke), 2 (Harke), 3 (Axt)
//
//
//
