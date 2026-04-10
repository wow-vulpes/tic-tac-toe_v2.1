package tictactoe.datasource.mapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class FieldConverter implements AttributeConverter<int[][], String> {
    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        if (attribute == null || attribute.length == 0 || attribute[0].length == 0){
            throw new IllegalArgumentException("There's no field");
        }

        StringBuilder stringBuilder = new StringBuilder(attribute[0].length * attribute.length);
        for (int[] row : attribute){
            for (int cell : row){
                stringBuilder.append((char)('0' + cell));
            }
        }

        return stringBuilder.toString();
    }

    @Override
    public int[][] convertToEntityAttribute(String dbData) {
        if (dbData == null){
            throw new IllegalArgumentException("There's no field");
        }

        //расчет на то, что поле квадратное
        int boardSize = (int) Math.sqrt(dbData.length());
        if (boardSize * boardSize != dbData.length()) {
            throw new IllegalArgumentException("Invalid field");
        }

        int[][] attribute = new int[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++){
            for (int j = 0; j < boardSize; j++){
                attribute[i][j] = dbData.charAt(i* boardSize + j) - '0';
            }
        }

        return attribute;
    }
}
