package tsygvintsev.watering_diary.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.entity.PlantType;
import tsygvintsev.watering_diary.repository.MaterialRepository;
import tsygvintsev.watering_diary.repository.PlantTypeRepository;

@Component
public class ReferencesSeeder implements CommandLineRunner {

    private final MaterialRepository materialRepository;
    private final PlantTypeRepository plantTypeRepository;

    public ReferencesSeeder(MaterialRepository materialRepository,
                          PlantTypeRepository plantTypeRepository) {
        this.materialRepository = materialRepository;
        this.plantTypeRepository = plantTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedMaterials();
        seedPlantTypes();
    }

    private void seedMaterials() {
        if (materialRepository.count() > 0) {
            System.out.println("Material already exists, skipping...");
            return;
        }

        System.out.println("Material loading...");

        materialRepository.save(new Material("Керамика", 30));
        materialRepository.save(new Material("Пластик", 70));
        materialRepository.save(new Material("Глина", 25));
        materialRepository.save(new Material("Терракота", 28));
        materialRepository.save(new Material("Металл", 80));
        materialRepository.save(new Material("Дерево", 40));
        materialRepository.save(new Material("Стекло", 50));
        materialRepository.save(new Material("Бетон", 35));
        materialRepository.save(new Material("Фарфор", 32));
        materialRepository.save(new Material("Кашпо с автополивом", 20));

        System.out.println("Material loaded: " + materialRepository.count());
    }

    private void seedPlantTypes() {
        if (plantTypeRepository.count() > 0) {
            System.out.println("Plant types already exists, skipping...");
            return;
        }

        System.out.println("Plant type loading...");

        plantTypeRepository.save(new PlantType("Кактус",
                "Семейство многолетних цветковых колючих растений. Кактусы представляют собой суккуленты с мясистыми стеблями, способными накапливать воду", 15));

        plantTypeRepository.save(new PlantType("Алоэ",
                "Род суккулентных растений с мясистыми листьями, собранными в прикорневые розетки. Многие виды обладают лечебными свойствами", 20));

        plantTypeRepository.save(new PlantType("Сансевиерия",
                "Род вечнозелёных бесстебельных растений с прямостоячими жёсткими листьями. Народное название тещин язык или щучий хвост", 22));

        plantTypeRepository.save(new PlantType("Толстянка",
                "Род суккулентных растений семейства Толстянковые. Популярное комнатное растение, известное как денежное дерево", 18));

        plantTypeRepository.save(new PlantType("Монстера",
                "Крупные тропические растения, лианы с крупными кожистыми листьями, покрытыми отверстиями и прорезями", 52));

        plantTypeRepository.save(new PlantType("Фикус",
                "Род растений семейства Тутовые, включает деревья и кустарники. В комнатном цветоводстве популярны виды с декоративной листвой", 48));

        plantTypeRepository.save(new PlantType("Драцена",
                "Род растений семейства Спаржевые. Древовидные или кустарниковые суккулентные растения с пучком листьев на верхушке", 45));

        plantTypeRepository.save(new PlantType("Хлорофитум",
                "Травянистое растение с узкими изогнутыми листьями и длинными свисающими побегами с дочерними розетками. Очищает воздух", 50));

        plantTypeRepository.save(new PlantType("Диффенбахия",
                "Вечнозелёное растение с крупными продолговатыми листьями, часто с пёстрой окраской и красивым узором", 55));

        plantTypeRepository.save(new PlantType("Замиокулькас",
                "Травянистое растение с сочными мясистыми корневищами и прямостоячими сложными листьями. Народное название долларовое дерево", 35));

        plantTypeRepository.save(new PlantType("Фиалка",
                "Низкорослое вечнозелёное травянистое растение с укороченными стеблями и розеткой листьев. Цветки разнообразной окраски собраны в кисти", 65));

        plantTypeRepository.save(new PlantType("Орхидея",
                "Крупнейшее семейство однодольных растений. Цветки отличаются разнообразием форм, размеров и окраски. Популярны виды рода Фаленопсис", 55));

        plantTypeRepository.save(new PlantType("Спатифиллум",
                "Многолетнее вечнозелёное растение с коротким корневищем. Соцветие початок на длинной ножке с белым покрывалом. Народное название женское счастье", 70));

        plantTypeRepository.save(new PlantType("Антуриум",
                "Вечнозелёные растения с кожистыми листьями разнообразной формы. Соцветие початок с ярким покрывалом красного цвета. Народное название мужское счастье", 68));

        plantTypeRepository.save(new PlantType("Бегония",
                "Род растений семейства Бегониевые. Травянистые растения или полукустарники с асимметричными листьями и яркими цветками", 62));

        plantTypeRepository.save(new PlantType("Герань",
                "Род семейства Гераниевые. Травянистые растения с ароматными листьями и яркими цветками, собранными в зонтичные соцветия", 58));

        plantTypeRepository.save(new PlantType("Папоротник",
                "Отдел сосудистых растений, характеризующихся крупными перисто-рассечёнными листьями. Нефролепис наиболее популярен в комнатной культуре", 80));

        plantTypeRepository.save(new PlantType("Традесканция",
                "Род многолетних вечнозелёных травянистых растений. Стелющиеся или свисающие побеги с очередными листьями, часто пёстроокрашенными", 72));

        plantTypeRepository.save(new PlantType("Хамедорея",
                "Род пальм из Центральной и Южной Америки. Невысокие пальмы с перистыми листьями, популярные в комнатном цветоводстве", 58));

        plantTypeRepository.save(new PlantType("Финиковая пальма",
                "Род растений семейства Пальмовые. Древесные растения с перистыми листьями. В комнатной культуре выращивают молодые экземпляры", 55));

        plantTypeRepository.save(new PlantType("Лимон",
                "Вид рода Цитрус семейства Рутовые. Небольшое вечнозелёное дерево с ароматными цветками и кислыми плодами", 65));

        plantTypeRepository.save(new PlantType("Базилик",
                "Однолетнее травянистое растение семейства Яснотковые. Пряное растение с ароматными листьями, используется в кулинарии", 70));

        plantTypeRepository.save(new PlantType("Мята",
                "Род многолетних травянистых растений семейства Яснотковые. Растения с сильным освежающим ароматом", 72));

        plantTypeRepository.save(new PlantType("Калатея",
                "Род растений семейства Марантовые. Многолетние корневищные травянистые растения с декоративными узорчатыми листьями", 58));

        plantTypeRepository.save(new PlantType("Плющ",
                "Род растений семейства Аралиевые. Вечнозелёные лазящие кустарники с угловато-лопастными листьями", 68));

        System.out.println("Plant types loaded: " + plantTypeRepository.count());
    }
}
