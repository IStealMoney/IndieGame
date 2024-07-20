package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import jdk.internal.jimage.ImageStrings;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    public static Gson gson = new GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    static Json json = new Json();
    public static ArrayList<Plant> plants = new ArrayList<Plant>();
    public static ArrayList<Integer[]> plantMapCoords = new ArrayList<Integer[]>();
    public static HashMap<Integer, ArrayList<Texture>> plantTextures = new HashMap<Integer, ArrayList<Texture>>();
    static ArrayList<Plant> plantsToRemove = new ArrayList<Plant>();

    public static ArrayList<Tile> mapTiles = new ArrayList<Tile>();
    public static HashMap<String, int[][]> maps = new HashMap<String, int[][]>();
    public static HashMap<String, Pixmap[][]> tilesetPixmaps = new HashMap<String, Pixmap[][]>();
    public static int tilesetSize = 512 / Main.TILE_SIZE;
    public static  String[] tilesets = {"ground", "destructible", "environment", "indestructible", "penguin", "customer"}; // add in according layer | first = lowest layer
    static ArrayList<Tile> tilesToRemove = new ArrayList<Tile>();

    public Map() {
        try {
            loadEditableMap();
            loadPlantTextures();
            loadPlants();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadPlants() {
        plants.clear();

        // Load plants
        ArrayList<LinkedTreeMap> plantsArray = new ArrayList<>();
        plantsArray = gson.fromJson(Gdx.files.internal("plants/plants_data.json").reader(), plantsArray.getClass());

        if (plantsArray == null) {
            return;
        }

        for (int i = 0; i < plantsArray.size(); i++) {
            LinkedTreeMap plant = plantsArray.get(i);
            String name = (String) plant.get("name");
            int id = (int) (double) plant.get("id");
            float x = (float) (double) plant.get("x");
            float y = (float) (double) plant.get("y");
            int mapX = (int) (double) plant.get("mapX");
            int mapY = (int) (double) plant.get("mapY");
            double duration = (double) plant.get("duration");
            double startDuration = (double) plant.get("startDuration");
            double value = (double) plant.get("duration");
            int maxGrowthState = (int) (double) plant.get("maxGrowthState");
            int growthState = (int) (double) plant.get("growthState");

            float mapXOffset = ((float) maps.get("ground")[mapY].length / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[0] / 2; // Half of the map
            float mapYOffset = ((float) maps.get("ground").length / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[1] / 2; // Half of the map

            Plant loadedPlant = new Plant(mapX, mapY, mapX * Main.TILE_SIZE * Main.MULTIPLIER - mapXOffset, ((maps.get("ground").length - 1) - mapY) * Main.TILE_SIZE * Main.MULTIPLIER - mapYOffset, id, name, duration, startDuration, value, maxGrowthState, growthState);
            plants.add(loadedPlant);
            plantMapCoords.add(new Integer[] {mapX, mapY});
        }
    }

    public static void savePlants() {
        ArrayList<HashMap<String, Object>> plantsArray = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < plants.size(); i++) {
            Plant plant = plants.get(i);
            plantsArray.add(new HashMap<>());
            plantsArray.get(i).put("name", plant.name);
            plantsArray.get(i).put("id", plant.id);
            plantsArray.get(i).put("mapX", plant.mapX);
            plantsArray.get(i).put("mapY", plant.mapY);
            plantsArray.get(i).put("x", plant.x);
            plantsArray.get(i).put("y", plant.y);
            plantsArray.get(i).put("duration", plant.duration);
            plantsArray.get(i).put("startDuration", plant.startDuration);
            plantsArray.get(i).put("value", plant.value);
            plantsArray.get(i).put("maxGrowthState", plant.maxGrowthState);
            plantsArray.get(i).put("growthState", plant.growthState);
        }

        String plantsString = gson.toJson(plantsArray);

        // save plant coords
        String plantMapCoordsString = gson.toJson(plantMapCoords);

        try {
            FileWriter fileWriter = new FileWriter(Gdx.files.internal("plants/plants_data.json").toString());
            fileWriter.write(plantsString);
            fileWriter.close();

            FileWriter fW = new FileWriter(Gdx.files.internal("plants/placed_plants_data.json").toString());
            fW.write(plantMapCoordsString);
            fW.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void loadPlantTextures() {
        plantTextures.clear();
        ArrayList plantsArray = new ArrayList<LinkedTreeMap>();
        plantsArray = gson.fromJson(Gdx.files.internal("plants/plants.json").reader(), plantsArray.getClass());
        ArrayList<HashMap<String, Object>> plantData = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < plantsArray.size(); i++) {
            plantData.add(gson.fromJson(String.valueOf(plantsArray.get(i)), new HashMap<String, Object>().getClass()));

            ArrayList<Texture> textures = new ArrayList<Texture>();

            // calculate textures from plant sprite sheet
            Texture plantSpritesheet = new Texture(Gdx.files.internal("plants/textures/" + plantData.get(i).get("name").toString() + "_spritesheet.png"));
            plantSpritesheet.getTextureData().prepare();
            Pixmap pm = plantSpritesheet.getTextureData().consumePixmap();

            // Split pixmap into tile pixmaps
            for (int j = 0; j < plantSpritesheet.getWidth() / Main.TILE_SIZE; j++) {
                Pixmap plantPixmap = new Pixmap(Main.TILE_SIZE, Main.TILE_SIZE, Pixmap.Format.RGBA8888);
                for (int y = 0; y < Main.TILE_SIZE; y++) {
                    for (int x = 0; x < Main.TILE_SIZE; x++) {
                        plantPixmap.drawPixel(x, y, pm.getPixel(x + (j * Main.TILE_SIZE), y));
                    }
                }
                textures.add(new Texture(plantPixmap));
            }

            plantTextures.put(i, textures);
        }
    }

    public static void loadEditableMap() throws IOException {
        maps.clear();
        mapTiles.clear();
        for (String tileset : tilesets) {
            // load tileset textures
            Texture tilesetTexture = new Texture(Gdx.files.internal("tiles/" + tileset + "_tileset.png"));

            Pixmap[][] pixmaps = new Pixmap[tilesetSize][tilesetSize]; // Because the tileset are 512x512

            tilesetTexture.getTextureData().prepare();
            Pixmap pm = tilesetTexture.getTextureData().consumePixmap();

            // Split pixmap into tile pixmaps
            for (int i = 0; i < tilesetSize; i++) {
                for (int j = 0; j < tilesetSize; j++) {
                    Pixmap tilePixmap = new Pixmap(Main.TILE_SIZE, Main.TILE_SIZE, Pixmap.Format.RGBA8888);
                    for (int y = 0; y < Main.TILE_SIZE; y++) {
                        for (int x = 0; x < Main.TILE_SIZE; x++) {
                            tilePixmap.drawPixel(x, y, pm.getPixel(x + (j * Main.TILE_SIZE), y + (i * Main.TILE_SIZE)));
                        }
                    }
                    pixmaps[i][j] = tilePixmap;
                }
            }
            // save them for global access
            tilesetPixmaps.put(tileset, pixmaps);

            String mapPath = "maps/map_" + tileset + ".csv";

            // Get map height
            BufferedReader br = new BufferedReader(new FileReader(Gdx.files.internal(mapPath).toString()));
            long mapLines = br.lines().count();

            // Load map from file
            BufferedReader bR = new BufferedReader(new FileReader(Gdx.files.internal(mapPath).toString()));
            ArrayList<ArrayList<Integer>> mapData = new ArrayList<ArrayList<Integer>>();

            int i = 0;
            String line = bR.readLine();

            while (line != null) {
                // Iterate through file lines and save its data
                mapData.add(new ArrayList<Integer>());
                String[] tilesString = line.split(",");
                int[] tiles = new int[tilesString.length];

                for (int j = 0; j < tiles.length; j++) {
                    tiles[j] = Integer.parseInt(tilesString[j]);
                    mapData.get(i).add(tiles[j]);

                    // Offset map so the player spawn in the center of it
                    float mapXOffset = ((float) tiles.length / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[0] / 2; // Half of the map
                    float mapYOffset = ((float) mapLines / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[1] / 2; // Half of the map

                    // Dont create tile, if type is -1
                    if (tiles[j] != -1) {
                        mapTiles.add(new Tile(tileset, new int[]{tiles[j] % tilesetSize, tiles[j] / tilesetSize}, (j * Main.TILE_SIZE * Main.MULTIPLIER) - mapXOffset,
                                ((mapLines - 1) - i) * Main.TILE_SIZE * Main.MULTIPLIER - mapYOffset, j, i, tiles[j]));
                    }

                }
                line = bR.readLine();
                i++;
            }

            int[][] map = new int[mapData.size()][mapData.get(0).size()];
            for (int y = 0; y < mapData.size(); y++) {
                for (int x = 0; x < mapData.get(y).size(); x++) {
                    map[y][x] = mapData.get(y).get(x);
                }
            }
            maps.put(tileset, map);
        }
    }


    public static void saveMap() {
        try {
            for (String tileset : tilesets) {
                // Iterate through existing map and write it to the file
                FileWriter fW = new FileWriter(Gdx.files.internal("maps/map_" + tileset + ".csv").toString());

                for (int i = 0; i < maps.get(tileset).length; i++) {
                    String lineString = "";
                    for (int j = 0; j < maps.get(tileset)[i].length; j++) {
                        lineString += maps.get(tileset)[i][j] + ",";
                    }

                    fW.write(lineString + "\n");
                    fW.flush();
                }
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
    public static void moveMap(float dx, float dy) {
        for (Tile tile: mapTiles) {
            tile.rect.x -= dx;
            tile.rect.y -= dy;
            tile.hitbox.x -= dx;
            tile.hitbox.y -= dy;
            tile.sprite.setPosition(tile.rect.x, tile.rect.y);
        }

        for (Plant plant : plants) {
            plant.x -= dx;
            plant.y -= dy;
            plant.rect.x -= dx;
            plant.rect.y -= dy;
        }
    }

    public static void draw(SpriteBatch batch) {
        //draw and update tiles except for environment
        for (Tile tile : mapTiles) {
            if (tile.type != -1 && !tile.tileset.equals("environment")) {
                tile.update();
                tile.draw(batch);
            } else if (tile.type == -1) {
                Map.tilesToRemove.add(tile);
            }
        }
        Map.mapTiles.removeAll(Map.tilesToRemove);
        Map.tilesToRemove.clear();

        // draw & update plants
        for (Plant plant : plants) {
            if (plant.id != -1) {
                Main.font.getData().setScale(1);
                plant.update();
                plant.draw(batch);
            } else {
                Map.plantsToRemove.add(plant);
            }
        }

        Map.plants.removeAll(plantsToRemove);
        plantsToRemove.clear();

        // draw & update environment tiles
        for (Tile tile : mapTiles) {
            if (tile.type != -1 && tile.tileset.equals("environment")) {
                tile.update();
                tile.draw(batch);
            } else if (tile.type == -1){
                Map.tilesToRemove.add(tile);
            }
        }

        Map.mapTiles.removeAll(Map.tilesToRemove);
        Map.tilesToRemove.clear();
    }
}