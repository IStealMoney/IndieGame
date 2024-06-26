package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Map {

    public static ArrayList<Tile> mapTiles = new ArrayList<Tile>();
    public static HashMap<String, int[][]> maps = new HashMap<String, int[][]>();
    public static HashMap<String, Pixmap[][]> tilesetPixmaps = new HashMap<String, Pixmap[][]>();
    public static int tilesetSize = 512 / Main.TILE_SIZE;
    public static  String[] tilesets = {"ground", "destructible", "environment", "indestructible"}; // add in according layer | first = lowest layer
    static ArrayList<Tile> tilesToRemove = new ArrayList<Tile>();

    public Map() {
        try {
            loadEditableMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadEditableMap() throws IOException {
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
    }

    public static void draw(SpriteBatch batch) {
        for (Tile tile : mapTiles) {
            if (tile.type != -1) {
                tile.update();
                tile.draw(batch);
            } else {
                Map.tilesToRemove.add(tile);
            }
        }
        Map.mapTiles.removeAll(Map.tilesToRemove);
        Map.tilesToRemove.clear();
    }
}
