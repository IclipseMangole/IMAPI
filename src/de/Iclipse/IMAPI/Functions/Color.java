package de.Iclipse.IMAPI.Functions;


import de.Iclipse.IMAPI.Data;
import de.Iclipse.IMAPI.IMAPI;
import de.Iclipse.IMAPI.Util.Command.IMCommand;
import org.bukkit.command.CommandSender;

/**
 * Created by Yannick who could get really angry if somebody steal his code!
 * ~Yannick on 08.06.2019 at 21:17 o´ clock
 */
public class Color {

    private final IMAPI imapi;

    public Color(IMAPI imapi){
        this.imapi = imapi;
    }

    @IMCommand(
            name = "Color",
            aliases = {"colour", "colors", "colours"},
            permissions = "im.cmd.color",
            description = "color.description",
            usage = "color.usage",
            maxArgs = 0
    )

    public void execute(CommandSender sender) {
        StringBuilder builder = new StringBuilder();
        builder.append(imapi.getData().getPrefix()).append("Colors/Farben:\n");
        builder.append("0 -> §0Black§7           1 -> §1Dark Blue§7\n");
        builder.append("2 -> §2Dark Green§7      3 -> §3Dark Aqua§7\n");
        builder.append("4 -> §4Dark Red§7        5 -> §5Dark Purple§7\n");
        builder.append("6 -> §6Gold§7            7 -> §7Grey§7\n");
        builder.append("8 -> §8Dark Grey§7       9 -> §9Blue§7\n");
        builder.append("a -> §aGreen§7           b -> §bAqua§7\n");
        builder.append("c -> §cRed§7             d -> §dLight Purple§7\n");
        builder.append("e -> §eYellow§7          f -> §fWhite§7\n");
        builder.append("k -> §k1!qEl§7           l -> §lBold§7\n");
        builder.append("m -> §mStrikethrough§7   n -> §nUnderline§7\n");
        builder.append("o -> §oItalic§7          r -> §rReset§7\n");
        sender.sendMessage(builder.toString());

    }
}
