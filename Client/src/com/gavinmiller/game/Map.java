package com.gavinmiller.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class to detail the maps and their fill tiles, as well as read and write the objects to and from text files
 * @author Gavin Miller
 * @version 0.1
 */
public class Map {

    private Tiles tileSet;
    private int fillTileID = -1;

    private ArrayList<MappedTile> mappedTiles = new ArrayList<>();
    private HashMap<Integer, String> comments = new HashMap<>();

    private File mapFile;

    public Map(File mapFile, Tiles tileSet) {
        this.mapFile = mapFile;

        this.tileSet = tileSet;

        try {
            Scanner scanner = new Scanner(mapFile);

            int currentLine = 0;

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    if (line.contains(":")) {
                        String[] splitString = line.split(":");
                        if (splitString[0].equalsIgnoreCase("fill")) {
                            fillTileID = Integer.parseInt(splitString[1]);
                            continue;
                        }
                    }

                    String[] bits = line.split(",");

                    if (bits.length >= 3) {
                        MappedTile mappedTile = new MappedTile(Integer.parseInt(bits[0]),
                                Integer.parseInt(bits[1]),
                                Integer.parseInt(bits[2])
                        );

                        mappedTiles.add(mappedTile);
                    }
                } else {
                    comments.put(currentLine, line);
                }

                currentLine++;
            }
        } catch (FileNotFoundException e) {
            //TODO: Deal with map file not being found
            e.printStackTrace();
        }
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        int tileWidth = 16 * xZoom;
        int tileHeight = 16 * yZoom;

        if (fillTileID >= 0) {
            Rectangle camera = renderer.getCamera();
            for (int y = camera.y - tileHeight - (camera.y % tileHeight); y < camera.y + camera.height; y += tileHeight) {
                for (int x = camera.x - tileWidth - (camera.x % tileWidth); x < camera.x + camera.width; x += tileWidth) {
                    tileSet.renderTile(fillTileID, renderer, x, y, xZoom, yZoom);
                }
            }

        }

        for (int index = 0; index < mappedTiles.size(); index++) {
            MappedTile mappedTile = mappedTiles.get(index);
            tileSet.renderTile(mappedTile.id, renderer, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom, yZoom);
        }
    }

    public void setTile(int tileX, int tileY, int tileID) {
        boolean foundTile = false;

        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            if (mappedTile.x == tileX && mappedTile.y == tileY) {
                mappedTile.id = tileID;
                //System.out.println("111");
                foundTile = true;
                break;
            }
        }

        if (!foundTile) {
            //System.out.println("222");
            //System.out.println("here");
            mappedTiles.add(new MappedTile(tileID, tileX, tileY));
        }
    }

    public void saveMap() {
        System.out.println("Saving map...");
        try {

            if (mapFile.exists()){
                mapFile.delete();
            }
            
            mapFile.createNewFile();

            PrintWriter printWriter = new PrintWriter(mapFile);

            int currentLine = 0;

            

            if (fillTileID >= 0) {
                if (comments.containsKey(currentLine)) {
                    printWriter.println(comments.get(currentLine));
                    currentLine++;
                }
                
                printWriter.println("Fill:" + fillTileID);
            }

            for (int i = 0; i < mappedTiles.size(); i++) {

                if (comments.containsKey(currentLine)) {
                    printWriter.println(comments.get(currentLine));
                }

                String tileID = "" + mappedTiles.get(i).id;
                String tileX = "" + mappedTiles.get(i).x;
                String tileY = "" + mappedTiles.get(i).y;
                printWriter.println(tileID + "," + tileX + "," + tileY);
                
                currentLine++;
            }

            printWriter.flush();
            printWriter.close();

            System.out.println("Map saved!");
            
            
        } catch (IOException ex) {
            //TODO: Deal with map saving exceptions
            ex.printStackTrace();
        }
    }
    
    public void removeTile(int tileX, int tileY){
        for (int i = 0; i < mappedTiles.size(); i++) {
            MappedTile mappedTile = mappedTiles.get(i);
            if (mappedTile.x == tileX && mappedTile.y == tileY) {
                mappedTiles.remove(i);
                return;
            }
        }
    }

    // Tile ID in the tileset and the position of the tile in the map
    class MappedTile {

        public int id, x, y;

        public MappedTile(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }
}
