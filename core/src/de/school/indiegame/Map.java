package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Map {

    public static int[][] map;
    public static ArrayList<Tile> mapTiles = new ArrayList<Tile>();

    public Map() {
        try {
            map = loadEditableMap("map");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] loadEditableMap(String mapName) throws IOException {
        // Get map height
        BufferedReader br = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + mapName + ".csv").toString()));
        long mapLines = br.lines().count();

        // Load map from file
        BufferedReader bR = new BufferedReader(new FileReader(Gdx.files.internal("maps/" + mapName + ".csv").toString()));
        ArrayList<ArrayList<Integer>> mapData = new ArrayList<ArrayList<Integer>>();

        int groundTileAmount = Gdx.files.internal("tiles/ground/").list().length;

        // Iterate through file lines and save its data
        int i = 0;
        String line = bR.readLine();
        while (line != null) {
            mapData.add(new ArrayList<Integer>());
            String[] tilesString = line.split(",");
            int[] tiles = new int[tilesString.length];
            for (int j = 0; j < tiles.length; j++) {
                tiles[j] = Integer.parseInt(tilesString[j]);
                mapData.get(i).add(tiles[j]);

                boolean isBlockable = false;
                String group = "ground";

                if (tiles[j] >= groundTileAmount) {  // check if type is above ground tiles limit
                    isBlockable = true;
                    group = "blockable";
                }

                float mapXOffset = (tiles.length / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[0] / 2; // Half of the map
                float mapYOffset = ((float) mapLines / 2) * Main.TILE_SIZE * Main.MULTIPLIER - Main.SCREEN_SIZE[1] / 2; // Half of the map
                // Offset map so the player spawn in the center of it
                mapTiles.add(new Tile((j * Main.TILE_SIZE * Main.MULTIPLIER) - mapXOffset,
                        ((mapLines - 1) - i) * Main.TILE_SIZE * Main.MULTIPLIER - mapYOffset, j, i, tiles[j], isBlockable, group));
            }
            line = bR.readLine();
            i++;
        }

        // set map to the loaded map data
        int[][] map = new int[mapData.size()][mapData.get(0).size()];
        for (int j = 0; j < mapData.size(); j++) {
            for (int k = 0; k < mapData.get(j).size(); k++) {
                map[j][k] = mapData.get(j).get(k);
            }
        }

        return map;
    }

    public static void saveMap(String mapName) {
        try {
            // Iterate through existing map and write it to the file
            FileWriter fW = new FileWriter(Gdx.files.internal("map/" + mapName + ".csv").toString());

            for (int i = 0; i < map.length; i++) {
                String lineString = "";
                for (int j = 0; j < map[i].length; j++) {
                    lineString += map[i][j] + ",";
                }

                fW.write(lineString + "\n");
                fW.flush();
            }
        } catch (IOException e) {
            System.out.println(e);
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
    }

    public static void draw(SpriteBatch batch) {
        for (Tile tile : mapTiles) {
            if (!tile.isBlockable) {
                tile.draw(batch);
                tile.update();
            }
        }
    }

    public static void drawBlockables(SpriteBatch batch) {
        for (Tile tile : mapTiles) {
            if (tile.isBlockable) {
                tile.draw(batch);
                tile.update();
            }
        }
    }
}
