package com.github.tytoyos.gibcoins.mixin.playerhider;

import com.github.tytoyos.gibcoins.playerhider.NearbyPlayerHiderRenderContext;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.item.equipment.EquipmentAsset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<S extends BipedEntityRenderState, A extends BipedEntityModel<S>> {
	@Shadow
	@Final
	private EquipmentRenderer equipmentRenderer;

	@Shadow
	private A getModel(S state, EquipmentSlot slot) {
		throw new AssertionError();
	}

	@Shadow
	private boolean usesInnerModel(EquipmentSlot slot) {
		throw new AssertionError();
	}

	@Inject(
		method = "renderArmor(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EquipmentSlot;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void gibcoins$handleArmorRendering(
		MatrixStack matrices,
		OrderedRenderCommandQueue queue,
		ItemStack stack,
		EquipmentSlot slot,
		int light,
		S renderState,
		CallbackInfo ci
	) {
		if (!NearbyPlayerHiderRenderContext.isPlayerHiderHidden(renderState)) {
			return;
		}

		ci.cancel();
		if (!NearbyPlayerHiderRenderContext.isPlayerHiderGhost(renderState)) {
			return;
		}

		EquippableComponent equippableComponent = stack.get(DataComponentTypes.EQUIPPABLE);
		if (equippableComponent == null || !ArmorFeatureRenderer.hasModel(stack, slot)) {
			return;
		}

		A model = this.getModel(renderState, slot);
		EquipmentModel.LayerType layerType = this.usesInnerModel(slot)
			? EquipmentModel.LayerType.HUMANOID_LEGGINGS
			: EquipmentModel.LayerType.HUMANOID;
		RegistryKey<EquipmentAsset> assetId = equippableComponent.assetId().orElseThrow();

		NearbyPlayerHiderRenderContext.setGhostArmorRender(true);
		try {
			this.equipmentRenderer.render(
				layerType,
				assetId,
				model,
				renderState,
				stack,
				matrices,
				queue,
				light,
				renderState.outlineColor
			);
		} finally {
			NearbyPlayerHiderRenderContext.setGhostArmorRender(false);
		}
	}
}
