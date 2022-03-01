package com.endremastered.endrem.world.structures;

import com.endremastered.endrem.config.ERConfig;
import com.endremastered.endrem.util.ERUtils;
import com.endremastered.endrem.world.util.CustomMonsterSpawn;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.structure.*;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.*;

import java.util.List;
import java.util.Random;

public class EndCastle extends StructureFeature<DefaultFeatureConfig> {

    public EndCastle(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    protected static boolean isFeatureChunk(StructureGeneratorFactory.Context<StructurePoolFeatureConfig> context) {
        BlockPos centerOfChunk = new BlockPos(chunkPos.x * 16, 0, chunkPos.z * 16);
        int landHeight = chunkGenerator.getHeightInGround(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Type.WORLD_SURFACE_WG, heightLimitView);
        VerticalBlockSample columnOfBlocks = chunkGenerator.getColumnSample(centerOfChunk.getX(), centerOfChunk.getZ(), heightLimitView);
        BlockState topBlock = columnOfBlocks.getState(centerOfChunk.up(landHeight));
        return ERUtils.getChunkDistanceFromSpawn(chunkPos) >= ERConfig.getData().END_CASTLE.spawnDistance && topBlock.getFluidState().isEmpty();
    }

    private static final List<CustomMonsterSpawn> STRUCTURE_MONSTERS = List.of(
            new CustomMonsterSpawn(EntityType.PILLAGER, 30, 30, 35),
            new CustomMonsterSpawn(EntityType.VINDICATOR, 20, 25, 30),
            new CustomMonsterSpawn(EntityType.EVOKER, 20, 10, 15),
            new CustomMonsterSpawn(EntityType.ILLUSIONER, 5, 5, 10)
    );

    @Override
    public Pool<SpawnSettings.SpawnEntry> getMonsterSpawns() {
        return CustomMonsterSpawn.getPoolFromList(STRUCTURE_MONSTERS);
    }

    public static class Start extends StructureStart<DefaultFeatureConfig> {
        public Start(StructureFeature<DefaultFeatureConfig> structureIn, ChunkPos chunkPos, int referenceIn, StructurePiecesList children) {
            super(structureIn, chunkPos, referenceIn,children);
        }

        @Override
        public void place(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox chunkBox, ChunkPos chunkPos) {

            BlockRotation rotation = BlockRotation.values()[random.nextInt(BlockRotation.values().length)];

            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;
            int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
            BlockPos newPos = new BlockPos(x, y + ERConfig.getData().END_CASTLE.height, z);
            EndCastlePieces.start(world.getServer().getStructureManager(),  newPos, rotation, this.getChildren());
            this.getBoundingBox();
        }
    }
}
