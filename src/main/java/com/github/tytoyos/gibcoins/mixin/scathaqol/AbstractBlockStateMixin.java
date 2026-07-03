package com.github.tytoyos.gibcoins.mixin.scathaqol;

import impl.qol.ScathaQol;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Inject(
        method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void gibcoins$extendOutlineShape(
        BlockView world,
        BlockPos pos,
        CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (ScathaQol.shouldUseFullBlockHitbox((BlockState)(Object)this)) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Inject(
        method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void gibcoins$extendOutlineShape(
        BlockView world,
        BlockPos pos,
        ShapeContext context,
        CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (ScathaQol.shouldUseFullBlockHitbox((BlockState)(Object)this)) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Inject(
        method = "getRaycastShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void gibcoins$extendRaycastShape(
        BlockView world,
        BlockPos pos,
        CallbackInfoReturnable<VoxelShape> cir
    ) {
        if (ScathaQol.shouldUseFullBlockHitbox((BlockState)(Object)this)) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
