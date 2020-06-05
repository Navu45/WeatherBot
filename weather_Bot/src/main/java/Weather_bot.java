import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Privacy;
import org.w3c.dom.Text;

import static org.telegram.abilitybots.api.objects.Flag.TEXT;
import static org.telegram.abilitybots.api.objects.Locality.ALL;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

public class Weather_bot extends AbilityBot {
    private static final String token = "1284039263:AAHSB4_TK71y4ZVHlcLikY9PRVY8Nt9Bt_8";
    private static final String name = "MyWeatherBot";
    private WeatherParser weatherParser = new WeatherParser();

    Weather_bot() {
        super(token, name);
    }

    @Override
    public int creatorId() {
        return 867274483;
    }

    public Ability start()
    {
        return Ability.builder()
                .name("start")
                .locality(ALL)
                .privacy(PUBLIC)
                .action(context -> silent.send("Hello!) Enter the city name to get the weather!",
                        context.chatId()))
                .build();
    }

    public Ability sendWeather() {
        return Ability.builder()
                .name(DEFAULT)
                .flag(TEXT)
                .privacy(PUBLIC)
                .locality(ALL)
                .input(0)
                .action((MessageContext context) ->{
                    silent.send("Current weather data:\n", context.chatId());
                        silent.send(weatherParser
                                .getCurrentWeather(context.firstArg()),context.chatId());
                }).build();

    }
}
