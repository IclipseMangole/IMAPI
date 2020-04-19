package de.Iclipse.IMAPI.Util;

import net.minecraft.server.v1_15_R1.Block;
import net.minecraft.server.v1_15_R1.MaterialMapColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.block.CraftBlock;

import java.util.HashMap;

public class MapRender extends Thread {
    private Location middle;
    private byte scale;
    private byte scaleBlocks;
    private byte[] data;
    private boolean finished;

    public MapRender(Location middle, byte scale) {
        this.middle = middle;
        this.scale = scale;
        this.scaleBlocks = (byte) Math.pow(2, scale);
        data = new byte[128 * 128];
    }

    public void run() {
        Location upperLeftCorner = middle.subtract(Math.pow(2, 8 + scale), 0.0, Math.pow(2, 8 + scale));
        for (int mapZ = 0; mapZ < 128; mapZ++) {
            for (int mapX = 0; mapX < 128; mapX++) {

                final byte[] mostOften = {(byte) MaterialMapColor.b.ac};
                int biggestAmount = 0;
                getColor(upperLeftCorner, (byte) mapX, (byte) mapZ, scaleBlocks).forEach((color, amount) -> {
                    if (amount > biggestAmount) {
                        mostOften[0] = color;
                    }
                });
                data[mapX + mapZ * 128] = mostOften[0];
                System.out.println("Field:" + (mapZ));
                System.out.println("Color: " + mostOften[0]);
            }
            System.out.println("Row: " + (mapZ + 1));
        }
        this.data = data;
        finished = true;
        this.interrupt();
    }

    public HashMap<Byte, Byte> getColor(Location corner, byte mapX, byte mapZ, byte size) {
        HashMap<Byte, Byte> blocks = new HashMap<>();
        for (int fieldX = 0; fieldX < size; fieldX++) {
            for (int fieldZ = 0; fieldZ < (int) size; fieldZ++) {
                Block b = ((CraftBlock) corner.getWorld().getHighestBlockAt(corner.add((mapX * (int) size) + fieldX, 0.0, (mapZ * size) + fieldZ))).getNMS().getBlock();
                System.out.println(corner.add((mapX * (int) size) + fieldX, 0.0, (mapZ * size) + fieldZ));
                byte color = (byte) b.e(null, null, null).ac;
                System.out.println("getColor: " + color);
                if (blocks.containsKey(color)) {
                    blocks.replace(color, (byte) (blocks.get(color) + 1));
                } else {
                    blocks.put(color, (byte) 1);
                }
                    /*
                } else {
                    byte color = (byte) MaterialMapColor.aa.ac;
                    if (blocks.containsKey(color)) {
                        blocks.replace(color, blocks.get(color));
                    } else {
                        blocks.put(color, (byte) 1);
                    }
                }
               */
            }
        }
        return blocks;
    }

    public boolean isFinished() {
        return finished;
    }

    public byte[] getData() {
        return data;
    }
}
