package io.github.KacyBiscuit.insomania.mixin;

import com.mojang.datafixers.util.Either;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Shadow
	public abstract ServerWorld getWorld();

	@Shadow
	public abstract void sendMessage(Text message, boolean actionBar);

	@Shadow
	public abstract void playerTick();

	@Inject(method = "trySleep",
			at = @At(value = "INVOKE",
					 target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSpawnPoint(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/math/BlockPos;FZZ)V"))
	void trySleep(BlockPos pos, CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> cir) {
		Vec3d vec3d = Vec3d.ofBottomCenter(pos);
		List<PhantomEntity> phantoms = this.getWorld().getEntitiesByClass(
				PhantomEntity.class,
				new Box(vec3d.getX() - 64.0, vec3d.getY() - 64.0, vec3d.getZ() - 64.0, vec3d.getX() + 64.0, vec3d.getY() + 64.0, vec3d.getZ() + 64.0),
				phantom -> phantom.canTarget((ServerPlayerEntity)(Object)this)
		);
		if(!phantoms.isEmpty()) {
			this.sendMessage(Text.translatable("insomania.sleep.phantoms"), true);
			cir.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
		}
		//System.out.println(phantoms);
	}
}
