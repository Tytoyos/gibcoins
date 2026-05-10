package com.github.tytoyos.gibcoins.mixin;

import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EquipmentRenderer.class)
public class EquipmentRendererMixin {
	@ModifyArg(
		method = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V"
		),
		index = 6
	)
	private int gibcoins$ghostArmorColor(int color) {
		return NearbyPlayerHiderRenderContext.isGhostArmorRenderActive()
			? NearbyPlayerHiderRenderContext.GHOST_COLOR
			: color;
	}
}
