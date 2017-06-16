import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.type.Include;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Lavoi on 2017/5/5.
 */
@Plugin(id = "test", name = "Test", version = "1.0")
public class test implements CommandExecutor {

    private HashMap<Player, Instant> blockingInstant = new HashMap<>();

    @Listener
    public void onServerStart(GameInitializationEvent event) {
        System.out.println("開始建構命令");
        CommandSpec myCommandSpec = CommandSpec.builder()
                .description(Text.of("Hello World Command"))
                .executor(new test())
                .build();

        System.out.println("註冊命令");
        Sponge.getCommandManager().register(this, myCommandSpec, "helloworld", "hello", "test");
    }
    @Override
    public CommandResult execute (CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("魔法大自爆 !!"));
        if (src instanceof Player) {
            Player player = (Player) src;
            player.damage(10000, DamageSource.builder().type(DamageTypes.MAGIC).build());
        }
        return CommandResult.success();
    }

    @Listener
    public void onABC(ClientConnectionEvent.Join event) {
        event.getTargetEntity().sendMessage(Text.of("嗨，歡迎加入伺服器!"));
//
//        if (event.getTargetEntity().getUniqueId().equals(UUID.fromString("3634adfee9034c0eb7d896a7fb9a4f46")))  {
//            event.getTargetEntity().sendMessage(Text.of("嗨87!"));
//        s}
    }

    @Listener
    public void onWAE(DamageEntityEvent event) {
        if (event.getCause().root() instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) event.getCause().root();
            if (entityDamageSource.getSource() instanceof Player) {
                Player player = (Player) entityDamageSource.getSource();
                if (player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                    ItemStack handItem = player.getItemInHand(HandTypes.MAIN_HAND).get();
                    if (handItem.getItem() == ItemTypes.WOODEN_SWORD) {
                        if (handItem.get(Keys.DISPLAY_NAME).isPresent()) {
                            if (handItem.get(Keys.DISPLAY_NAME).get().toPlain().equals("超強神劍")) {
                                event.setBaseDamage(500);
                                player.sendMessage(Text.of("造成500點傷害!"));
                            }
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void onDEF (DamageEntityEvent event){
        if (event.getCause().root() instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) event.getCause().root();
            if (entityDamageSource.getSource() instanceof Living) {
                Living attacker = (Living) entityDamageSource.getSource();

                if (event.getTargetEntity() instanceof Player) {
                    Player defender = (Player) event.getTargetEntity();
                    if (defender.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
                        ItemStack handItem = defender.getItemInHand(HandTypes.OFF_HAND).get();
                        if (handItem.getItem() == ItemTypes.SHIELD) {
                            if (handItem.get(Keys.DISPLAY_NAME).isPresent()) {
                                if (handItem.get(Keys.DISPLAY_NAME).get().toPlain().equals("尖刺盾")) {
                                    if (  Instant.now().isBefore(  blockingInstant.get(defender).plusSeconds(1)   )) {
                                        attacker.damage(1000, DamageSource.builder().type(DamageTypes.ATTACK).build());
                                        defender.sendMessage(Text.of("防禦成功!"));
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    @Listener
    public void onDEF2 (DamageEntityEvent event){
        if (event.getCause().root() instanceof EntityDamageSource) {
            EntityDamageSource entityDamageSource = (EntityDamageSource) event.getCause().root();
            if (entityDamageSource.getSource() instanceof Player) {
                Player attacker = (Player) entityDamageSource.getSource();

                if (event.getTargetEntity() instanceof Player) {
                    Player defender = (Player) event.getTargetEntity();
                    if (defender.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
                        ItemStack handItem = defender.getItemInHand(HandTypes.OFF_HAND).get();
                        if (handItem.getItem() == ItemTypes.SHIELD) {
                            if (handItem.get(Keys.DISPLAY_NAME).isPresent()) {
                                if (handItem.get(Keys.DISPLAY_NAME).get().toPlain().equals("尖刺盾")) {
                                    if (  Instant.now().isBefore(  blockingInstant.get(defender).plusSeconds(1)   )) {
                                        attacker.damage(1000, DamageSource.builder().type(DamageTypes.ATTACK).build());
                                        defender.sendMessage(Text.of("防禦成功!"));
                                        event.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Listener
    public void onSDF(InteractItemEvent.Secondary.OffHand event){
        blockingInstant.put((Player) event.getCause().root(), Instant.now());
    }










}
