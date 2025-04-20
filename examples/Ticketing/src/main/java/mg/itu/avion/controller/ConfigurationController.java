package mg.itu.avion.controller;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.POST;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.Role;
import com.ETU1792.utils.ModelView;
import com.ETU1792.utils.MySession;

import mg.itu.avion.config.ConfigKey;
import mg.itu.avion.config.Configuration;
import mg.itu.avion.config.ConfigurationRepository;
import mg.itu.avion.config.ConfigurationService;
import mg.itu.avion.utils.MyBatisUtil;

@Controller
@Authentified
@Role("ADMIN")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController() {
        this.configurationService = new ConfigurationService(
            new ConfigurationRepository(MyBatisUtil.getSqlSessionFactory())
        );
    }

    @GET("admin/configs")
    public ModelView pageConfig(MySession session) {
        ModelView mv = new ModelView("/layouts/sidebar.jsp");
        mv.addObject("contentJsp", "/admin/config/index.jsp");
        mv.addObject("activeMenu", "configs");
        mv.addObject("pageTitle", "Paramètres");

        java.util.Map<String, Configuration> map = new java.util.HashMap<>();
        for (ConfigKey key : ConfigKey.values()) {
            Configuration c = configurationService.getConfigurationByKey(key.name().toLowerCase());
            map.put(key.name(), c);
        }
        
        mv.addObject("configMap", map);
        return mv;
    }

    @POST("admin/configs/save")
    public ModelView saveConfig(
            @Param(name = "reservationCutoffHours") Integer reservationCutoffHours,
            @Param(name = "cancellationCutoffHours") Integer cancellationCutoffHours,
            @Param(name = "promotionLimit") Integer promotionLimit,
            @Param(name = "promotionDiscount") String promotionDiscount
    ) {
        // Validation basique
        if (reservationCutoffHours == null || reservationCutoffHours < 0) {
            throw new IllegalArgumentException("Heures de coupure de réservation invalides");
        }
        if (cancellationCutoffHours == null || cancellationCutoffHours < 0) {
            throw new IllegalArgumentException("Heures de coupure d'annulation invalides");
        }
        if (promotionLimit != null && promotionLimit < 0) {
            throw new IllegalArgumentException("Limite de promotion invalide");
        }
        if (promotionDiscount == null || promotionDiscount.isEmpty()) {
            throw new IllegalArgumentException("Remise de promotion invalide");
        }
        if (!promotionDiscount.matches("\\d+(\\.\\d+)?")) {
            throw new IllegalArgumentException("Remise de promotion doit être un nombre décimal");
        }

        // Upsert des 4 clés
        upsert(ConfigKey.RESERVATION_CUTOFF_HOURS,   reservationCutoffHours == null ? null : reservationCutoffHours.toString());
        upsert(ConfigKey.CANCELLATION_CUTOFF_HOURS,  cancellationCutoffHours == null ? null : cancellationCutoffHours.toString());
        upsert(ConfigKey.PROMOTION_LIMIT,            promotionLimit == null ? null : promotionLimit.toString());
        upsert(ConfigKey.PROMOTION_DISCOUNT,         promotionDiscount); // garde la string (ex: "12.5")

        ModelView mv = new ModelView("admin/configs");
        mv.setIsRedirect(true);
        return mv;
    }

    private void upsert(ConfigKey key, String value) {
        if (value == null) return;
        Configuration cfg = Configuration.builder()
                .configKey(key)
                .configValue(value)
                .description(key.getDescription())
                .build();
        configurationService.upsert(cfg);
    }
}

