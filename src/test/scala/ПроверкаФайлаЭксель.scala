import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.{DoNotDiscover, BeforeAndAfterAll, FreeSpec, Matchers}

/**
  * Created by smakhetov on 30.08.2016.
  * Тест проверяет значение полей таблицы Excel.
  * В начале описываются координаты и ожидаемые значения ячеек в таблице,
  * Далее для каждой строки таблицы производится извлечение значения и сверка
  */

@DoNotDiscover
class ПроверкаФайлаЭксель extends FreeSpec with Matchers with TableDrivenPropertyChecks with BeforeAndAfterAll{

  val excelCheckTable = Table (
    ("Лист", "Строка", "Столбец", "Ожидаемое значение"),
    (0,18,2, "GoodItem1"),
    (0,21,2, "GoodItme3")
  )

  val fileName = "taskfiles/Черновик подтверждения заказа №NN01 от 04.12.2011.xlsx"
  val myExcelBook = new XSSFWorkbook(new FileInputStream(fileName))
  // Извлечение значения ячейки по координатам
  def cellValue(sheet: Int, row: Int, col: Int): String = myExcelBook.getSheetAt(sheet).getRow(row).getCell(col).getStringCellValue



  "Проверка значений в таблице Excel" - {
    forAll(excelCheckTable) {(sheet: Int,row: Int,col: Int,value: String) =>
      s"""Значение ячейки на листе $sheet в строке $row в колонке $col должно быть "$value"""" in {
        cellValue(sheet,row,col) should be (value)
      }
    }
  }

  override def afterAll(){//Код выполняется после всех шагов
    myExcelBook.close()
  }
}
