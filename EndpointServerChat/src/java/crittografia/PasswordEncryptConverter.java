package crittografia;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 
 * @author I_Particolari
 */
@Converter
public class PasswordEncryptConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return AES.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la crittografia della password", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return AES.decrypt(dbData);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la decrittografia della password", e);
        }
    }
}
