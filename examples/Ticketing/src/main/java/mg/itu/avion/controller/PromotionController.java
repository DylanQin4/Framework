package mg.itu.avion.controller;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.JSON;
import com.ETU1792.annotation.Param;
import com.ETU1792.annotation.Role;

import java.time.LocalDateTime;
import java.util.List;

import mg.itu.avion.promotion.Promotion;
import mg.itu.avion.promotion.PromotionRepository;
import mg.itu.avion.promotion.PromotionService;

@Controller
// @Authentified
// @Role("ADMIN")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController() {
        var factory = mg.itu.avion.utils.MyBatisUtil.getSqlSessionFactory();
        this.promotionService = new PromotionService(
            new PromotionRepository(factory)
        );
    }

    @GET("api/admin/promotions/report")
    // @JSON
    public void getPromotionsBefore(@Param(name = "before") LocalDateTime before) {
        promotionService.makePromotionReport(before);
    }
}