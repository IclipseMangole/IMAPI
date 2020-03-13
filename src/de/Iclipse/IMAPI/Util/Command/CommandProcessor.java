package de.Iclipse.IMAPI.Util.Command;


import com.google.common.base.Defaults;
import com.google.common.base.Joiner;
import de.Iclipse.IMAPI.Util.TypeUtils;
import de.Iclipse.IMAPI.Util.executor.ThreadExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static de.Iclipse.IMAPI.Data.dsp;


public class CommandProcessor<S> {
    private GlobalCommand<S> masterCommand;
    private IMCommand command;
    private Object function;
    private Method method;
    private Class[] methodParameters;

    public CommandProcessor(GlobalCommand<S> masterCommand, IMCommand command, Object function, Method method) {
        this.masterCommand = masterCommand;
        this.command = command;
        this.function = function;
        this.method = method;
        this.methodParameters = method.getParameterTypes();
    }

    public void prepareProcess(S sender, int state, String[] args) {
        FlagList.FilterResult flags = null;
        try {
            flags = FlagList.generate(command, args);
        } catch (FlagNotFoundException e) {
            //TODO masterCommand.getDispatcher().warning(sender, "warning.wrongflag", masterCommand.getDispatcher().buildList(command.flags()));
        }
        List<String> wildcards = new LinkedList<>();
        for (int i = 0; i < command.parent().length; i++) {
            String parentCommand = command.parent()[i];
            if (parentCommand.equals("%")) {
                wildcards.add(args[i - 1]);
            }
        }
        if (command.name().equals("%")) {
            wildcards.add(args[args.length - 1]);
        }
        process(sender, flags, wildcards.size(), TypeUtils.appendArrays(wildcards.toArray(new String[wildcards.size()]), Arrays.copyOfRange(args, state, args.length)));
    }

    public void process(S sender, FlagList.FilterResult flags, int wildcards, String[] args) {
        if (command.noConsole() && !masterCommand.getPlayerClass().isInstance(sender)) {
            dsp.send((CommandSender) sender, "cmd.noconsole");
            return;
        }

        if (command.requiresConsole() && masterCommand.getPlayerClass().isInstance(sender)) {
            dsp.send((Player) sender, "cmd.consoleonly");
            return;
        }

        //Permission checken
        boolean hasPermission = false;
        if (command.permissions().length > 0) {
            for (String permission : command.permissions()) {
                if (masterCommand.checkPermission(sender, permission) || masterCommand.checkPermission(sender, "aw." + permission)) {
                    hasPermission = true;
                    break;
                }
            }
        } else {
            List<String> permParts = new ArrayList<>(Arrays.asList(command.parent()));
            permParts.add(command.name());
            String permission = permParts.stream().map(perm -> perm.equals("%") ? "any" : perm).collect(Collectors.joining("."));
            hasPermission = masterCommand.checkPermission(sender, "aw." + permission);
        }
        if (!hasPermission) {
            dsp.send((Player) sender, "cmd.noperm");
            return;
        }

        //Parameter mit denen der command aufgerufen wird
        final Object[] params = new Object[methodParameters.length];
        params[0] = sender;
        int start = 0;

        //Flags filtern
        if (flags != null) {
            params[1] = flags.getFlags();
            args = flags.getArgs();
            start = 1;
        }

        //Argumentanzahl
        if (command.minArgs() > -1 && args.length - wildcards < command.minArgs() || command.maxArgs() > -1 && args.length - wildcards > command.maxArgs()) {
            dsp.send((Player) sender, "cmd.usage", command.usage());
            return;
        }

        int j = 0;

        //Parameter nach Typ filtern
        for (int i = start + 1; i < methodParameters.length; i++) {
            try {
                if (j < args.length) {
                    if (methodParameters[i] == String.class) {
                        params[i] = args[j];
                    } else {
                        params[i] = TypeUtils.convert(methodParameters[i], args[j]);
                    }
                } else {
                    params[i] = Defaults.defaultValue(methodParameters[i]);
                }
                j++;
            } catch (Exception ex) {
                dsp.send((Player) sender, "cmd.usage", command.usage());
                return;
            }
        }

        //übrige Argumente zu einem String zusammenfügen
        if (args.length > methodParameters.length - (2 + start) && methodParameters[methodParameters.length - 1] == String.class) {
            params[methodParameters.length - 1] = Joiner.on(' ').join(Arrays.asList(args).subList(methodParameters.length - (2 + start), args.length));
        }

        //command Ausführen
        if (command.runAsync()) {
            ThreadExecutor.executeAsync(() -> execute(sender, params));
        } else {
            execute(sender, params);
        }
    }

    private void execute(S sender, Object[] params) {
        try {
            Object result = method.invoke(function, params);
            if (result instanceof Boolean) {
                boolean booleanResult = (boolean) result;
                if (!booleanResult) {
                    dsp.send((Player) sender, "cmd.usage", command.usage());
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public IMCommand getCommand() {
        return command;
    }
}
