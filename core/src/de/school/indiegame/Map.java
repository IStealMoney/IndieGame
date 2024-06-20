package de.school.indiegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.*;
import java.util.ArrayList;

public class Map {

    public static int[][] map;
    public static ArrayList<Tile> mapTiles = new ArrayList<Tile>();

    public Map() {
        try {
            map = loadEditableMap("map-1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int[][] loadEditableMap(String mapName) throws IOException {
        BufferedReader bR = new BufferedReader(new FileReader(Gdx.files.internal("map/map-1.csv").toString()));
        ArrayList<ArrayList<Integer>> mapData = new ArrayList<>();
        
        int i = 0;
        String line = bR.readLine();
        while (line != null) {
            mapData.add(new ArrayList<Integer>());
            String[] tilesString = line.split(",");
            int[] tiles = new int[tilesString.length];
            for (int j = 0; j < tiles.length; j++) {
                tiles[j] = Integer.parseInt(tilesString[j]);
                mapData.get(i).add(tiles[j]);
                mapTiles.add(new Tile(j * Main.TILE_SIZE * Main.MULTIPLIER, i * Main.TILE_SIZE * Main.MULTIPLIER, tiles[j]));
            }
            line = bR.readLine();
            i++;
        }
        int[][] map = new int[mapData.size()][mapData.get(0).size()];
        for (int j = 0; j < mapData.size(); j++) {
            for (int k = 0; k < mapData.get(j).size(); k++) {
                map[j][k] = mapData.get(j).get(k);
            }
        }
        return map;
    }

    public static void moveMap(float dx, float dy) {
        for (Tile tile: mapTiles) {
            tile.rect.x -= dx;
            tile.rect.y -= dy;
        }

    }

    public static void draw(SpriteBatch batch) {
        for (Tile tile : mapTiles) {
            tile.draw(batch);
            tile.update();
        }
    }
}
