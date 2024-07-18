package de.school.indiegame;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


// Main doesn't contain any logic or rendering code bcs it extends Game
// class for shared resources

public class Main extends Game {
	public static int[] GAME_SIZE = {480, 270};
	public static float MULTIPLIER;
	public static float[] SCREEN_SIZE = new float[2];
	public static int TILE_SIZE = 16;
	public static Map MAP;
	public static Inventory inventory;
	public static MoneySystem moneySystem;
	public static Customer customer;

	public static SpriteBatch batch;
	public static ShapeRenderer shape;
	public static Player player;
	public static Tool tool;
	public static Toolbar toolbar;
	public static StartScreen startScreen;
	public static PauseScreen pauseScreen;
	public static GameScreen gameScreen;
	public static MenuBird menuBird;
	public static boolean mouseAboveHud = false;
	public static BitmapFont font;
	public static BitmapFont menuFont;
	public static BitmapFont moneyFont;
	public static BitmapFont plantFont;
	public static BitmapFont costumerFont;
	public static BitmapFont activeItemFont;
	public static FreeTypeFontGenerator fontGenerator;
	public static FreeTypeFontParameter fontParameter;
	public static Skin skin;

	@Override
	public void create () {
		initializeGame();
	}

	public void initializeGame() {
		// Setup Font
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/vcr_osd_mono.ttf"));
		fontParameter = new FreeTypeFontParameter();
		fontParameter.borderWidth = 2;
		fontParameter.size = 16;
		font = fontGenerator.generateFont(fontParameter);
		fontParameter.size = 120;
		fontParameter.borderWidth = 3;
		menuFont = fontGenerator.generateFont(fontParameter);

		fontParameter.size = 64;
		fontParameter.borderWidth = 2;
		moneyFont = fontGenerator.generateFont(fontParameter);

		fontParameter.borderWidth = 1;
		fontParameter.size = 15;
		plantFont = fontGenerator.generateFont(fontParameter);

		fontParameter.borderWidth = 2;
		fontParameter.size = 54;
		costumerFont = fontGenerator.generateFont(fontParameter);

		fontParameter.borderWidth = 2;
		fontParameter.size = 16;
		activeItemFont = fontGenerator.generateFont(fontParameter);

		gameScreen = new GameScreen(this);
		pauseScreen = new PauseScreen(this);
		startScreen = new StartScreen(this);
		setScreen(new StartScreen(this));

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SCREEN_SIZE[0] = Gdx.graphics.getWidth();
		SCREEN_SIZE[1] = Gdx.graphics.getHeight();
		MULTIPLIER = (SCREEN_SIZE[0] / GAME_SIZE[0] + SCREEN_SIZE[1] / GAME_SIZE[1]) / 2;
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		shape.setAutoShapeType(true);
		player = new Player(SCREEN_SIZE[0] / 2, SCREEN_SIZE[1] / 2);
		tool = new Tool(player.rect.x, player.rect.y, 2);
		toolbar = new Toolbar();
		MAP = new Map();

		inventory = new Inventory(SCREEN_SIZE[0] - SCREEN_SIZE[0] / 2.4f, SCREEN_SIZE[1] / 2.5f);
		moneySystem = new MoneySystem();
		customer = new Customer();
		menuBird = new MenuBird();
	}
	
	@Override
	public void dispose () {
		// Dispose every texture here
		batch.dispose();
		font.dispose();
		player.texture.dispose();
		tool.texture.dispose();
		Toolbar.toolbarTexture.dispose();
		Toolbar.selectSlotTexture.dispose();
		Inventory.backgroundTexture.dispose();
		inventory.selectedSlotTexture.dispose();
		MoneySystem.coinTexture.dispose();
		Customer.cusInvTexture.dispose();
		Main.menuBird.texture.dispose();

		for (Tile tile : Map.mapTiles) {
			tile.texture.dispose();
			tile.sprite.getTexture().dispose();
		}

		for (Plant plant : Map.plants) {
			plant.texture.dispose();
		}

		for (int i = 0; i < Inventory.itemTextures.size(); i++) {
			Inventory.itemTextures.get(i).dispose();
		}
	}

	public void showPauseScreen() {
		setScreen(pauseScreen);
	}

	public void showGameScreen() {
		setScreen(gameScreen);
	}

	public void showStartScreen() {
		setScreen(startScreen);
	}

	public void resetGame() {
		// dispose current screens
		if (gameScreen != null) {
			gameScreen.dispose();
		}
		if (pauseScreen != null) {
			pauseScreen.dispose();
		}
		if (startScreen != null) {
			startScreen.dispose();
		}
		// new Instance
		initializeGame();
	}

	public void saveGame() {
		Map.savePlants();
		Map.saveMap();
		MoneySystem.saveMoney();
		inventory.saveInventory();
	}
}
