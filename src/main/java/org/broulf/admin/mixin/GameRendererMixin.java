/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package org.broulf.admin.mixin;

import org.broulf.admin.AdminClient;
import org.broulf.admin.event.EventManager;
import org.broulf.admin.mixinterface.IGameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.SynchronousResourceReloadListener;
import net.minecraft.util.Identifier;
import org.broulf.admin.events.CameraTransformViewBobbingListener.CameraTransformViewBobbingEvent;
import org.broulf.admin.events.HitResultRayTraceListener.HitResultRayTraceEvent;
import org.broulf.admin.events.RenderListener.RenderEvent;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin
	implements AutoCloseable, SynchronousResourceReloadListener, IGameRenderer
{
	@Redirect(at = @At(value = "INVOKE",
		target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V",
		ordinal = 0),
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"})
	private void onRenderWorldViewBobbing(GameRenderer gameRenderer,
		MatrixStack matrixStack, float partalTicks)
	{
		CameraTransformViewBobbingEvent event =
			new CameraTransformViewBobbingEvent();
		EventManager.fire(event);
		
		if(event.isCancelled())
			return;
		
		bobView(matrixStack, partalTicks);
	}
	
	@Inject(
		at = {@At(value = "FIELD",
			target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0)},
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"})
	private void onRenderWorld(float partialTicks, long finishTimeNano,
		MatrixStack matrixStack, CallbackInfo ci)
	{
		RenderEvent event = new RenderEvent(partialTicks);
		EventManager.fire(event);
	}
	
	@Redirect(
		at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/options/GameOptions;fov:D",
			opcode = Opcodes.GETFIELD,
			ordinal = 0),
		method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"})
	private double getFov(GameOptions options)
	{
		return AdminClient.INSTANCE.getOtfs().zoomOtf
			.changeFovBasedOnZoom(options.fov);
	}
	
	@Inject(at = {@At(value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;getCameraPosVec(F)Lnet/minecraft/util/math/Vec3d;",
		opcode = Opcodes.INVOKEVIRTUAL,
		ordinal = 0)}, method = {"updateTargetedEntity(F)V"})
	private void onHitResultRayTrace(float float_1, CallbackInfo ci)
	{
		HitResultRayTraceEvent event = new HitResultRayTraceEvent(float_1);
		EventManager.fire(event);
	}
	
	@Redirect(
		at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F",
			ordinal = 0),
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"})
	private float wurstNauseaLerp(float delta, float first, float second)
	{
		return 0;
	}
	
	@Inject(at = {@At("HEAD")},
		method = {
			"bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"},
		cancellable = true)
	private void onBobViewWhenHurt(MatrixStack matrixStack, float f,
		CallbackInfo ci)
	{
		if(AdminClient.INSTANCE.getHax().noHurtcamHack.isEnabled())
			ci.cancel();
	}
	
	@Shadow
	private void bobView(MatrixStack matrixStack, float partalTicks)
	{
		
	}
	
	@Override
	public void loadWurstShader(Identifier identifier)
	{
		loadShader(identifier);
	}
	
	@Shadow
	private void loadShader(Identifier identifier)
	{
		
	}
}