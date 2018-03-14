package be.mielnoelanders.bazinga.service;

import be.mielnoelanders.bazinga.domain.SupplierGames;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SupplierGamesServiceIT {

    @Autowired
    private SupplierGamesService gameService;

    @Test
    public void createReadUpdateDeleteTestIT(){//De IT toevoeging zorgt ervoor dat dat de test later in de buildfase gerund wordt.

        // BUILD the game
        SupplierGames supplierGames1 = new SupplierGames();
        supplierGames1.setDate("23/01/2018");

        // CREATE TEST
        SupplierGames inserted = this.gameService.insertSupplierGames(supplierGames1);
        assertTrue(inserted.getDate().equals("23/01/2018"));
        assertFalse(inserted.getId()==0);
        Long id = inserted.getId();

        // READ TEST
        SupplierGames foundWithGetOne = this.gameService.getOne(id);
        assertEquals("Id komt niet overeen",inserted.getDate(), foundWithGetOne.getDate());

        // UPDATE TEST
        foundWithGetOne.setDate("09/03/2018");
        SupplierGames foundAfterSave = this.gameService.changeSupplierGames(id,foundWithGetOne);
        assertEquals("Melding komt niet overeen","09/03/2018",foundAfterSave.getDate());

        // DELETE TEST
        this.gameService.deleteById(id);
        assertNull(this.gameService.getOne(id));
    }

}