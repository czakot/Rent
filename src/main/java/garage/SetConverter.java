/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package garage;

import com.rent.entity.Role;
import java.util.EnumSet;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 *
 * @author czakot
 */
@Converter
class SetConverter implements AttributeConverter<EnumSet<Role>, String>{

    @Override
    public String convertToDatabaseColumn(EnumSet<Role> x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EnumSet<Role> convertToEntityAttribute(String y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
