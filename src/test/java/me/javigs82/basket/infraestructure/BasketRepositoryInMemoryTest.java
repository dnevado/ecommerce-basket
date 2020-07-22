package me.javigs82.basket.infraestructure;

import io.quarkus.test.junit.QuarkusTest;
import me.javigs82.basket.domain.Basket;
import me.javigs82.basket.domain.Item;
import me.javigs82.basket.infrastructure.BasketRepositoryInMemory;
import me.javigs82.basket.infrastructure.ItemAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


@QuarkusTest
public class BasketRepositoryInMemoryTest {

    @Inject
    BasketRepositoryInMemory basketRepository;

    @Inject
    ItemAdapter itemAdapter;

    @Test
    public void testCreateBasket() {
        Optional<Basket> basket = createBasket();
        basket.ifPresentOrElse(b1 -> {
                    this.basketRepository
                            .getBasketByCode(b1.getCode())
                            .ifPresentOrElse(b2 -> {
                                Assertions.assertEquals(b1.getCode(), b2.getCode());
                                Assertions.assertEquals(b1.getItems().size(), 0);
                                Assertions.assertEquals(b1.getPriceNumber(), BigDecimal.ZERO);
                                    },
                                    () -> Assertions.fail());
                },
                () -> Assertions.fail()
        );
    }

    @Test
    public void testDeleteBasket() {
        Optional<Basket> basket = createBasket();
        basket.ifPresentOrElse(b1 -> {
                    this.basketRepository
                            .deleteBasket(b1.getCode())
                            .ifPresentOrElse(b2 -> Assertions.assertEquals(b1.getCode(), b2.getCode()),
                                    () -> Assertions.fail());
                },
                () -> Assertions.fail()
        );
    }

    @Test
    public void testDeleteBasketNotExist() {
        Optional<Basket> basketDeleted = this.basketRepository
                .deleteBasket("not_exist_code");
        basketDeleted.ifPresentOrElse(
                b -> Assertions.fail(),
                () -> Assertions.assertTrue(true)
        );
    }

    @Test
    public void testGetBasketByCode() {
        Optional<Basket> basketCreated = createBasket();
        basketCreated.ifPresentOrElse(
                b1 -> this.basketRepository
                        .getBasketByCode(b1.getCode()).ifPresentOrElse(
                                b2 -> Assertions.assertEquals(b1.getCode(), b2.getCode()),
                                () -> Assertions.fail()
                        ),
                () -> Assertions.fail()
        );
    }

    @Test
    public void testGetBasketByCodeNotExist() {
        Optional<Basket> basketGot = this.basketRepository
                .getBasketByCode("not_exist_code");
        basketGot.ifPresentOrElse(
                b -> Assertions.fail(),
                () -> Assertions.assertTrue(true)
        );
    }

    @Test
    public void testAddItemToBasket() {
        Optional<Basket> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode("TSHIRT");
        basketCreated.ifPresent(b -> {
            item.ifPresent(i -> {
                this.basketRepository.addItemToBasket(b.getCode(), i)
                        .ifPresentOrElse(
                                b2 -> Assertions.assertTrue(b.getItems().contains(i)),
                                () -> Assertions.fail()
                        );
            });
        });

    }

    @Test
    public void testGetPriceOneItemToBasket() {
        Optional<Basket> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode("TSHIRT");
        basketCreated.ifPresent(b -> {
            item.ifPresent(i -> {
                this.basketRepository.addItemToBasket(b.getCode(), i)
                        .ifPresentOrElse(
                                b2 -> {
                                    Assertions.assertTrue(b.getItems().contains(i));
                                    Assertions.assertEquals(b.getPriceNumber(), BigDecimal.valueOf(i.getPrice()));
                                },
                                () -> Assertions.fail()
                        );
            });
        });
    }

    @Test
    public void testGetPriceRandomItemToBasket() {
        Optional<Basket> basketCreated = createBasket();
        Optional<Item> item = this.itemAdapter.getItemByCode("TSHIRT");
        int randomNum = ThreadLocalRandom.current().nextInt(1, 500 + 1);
        basketCreated.ifPresent(b -> {
            item.ifPresent(it -> {
                for (int j = 0; j < randomNum; j ++) {
                    this.basketRepository.addItemToBasket(b.getCode(), it);
                }
                Assertions.assertEquals(b.getPriceNumber(), BigDecimal.valueOf(it.getPrice() * randomNum));
            });
        });
    }

    private Optional<Basket> createBasket() {
        return this.basketRepository.createBasket("This is my basket");
    }
}