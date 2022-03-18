package mx.com.adoptame.entities.character;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ReflectionUtils;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
   @Autowired
   private CharacterRepository characterRepository;
   
   public List<Character> findAll() {
       return (List<Character>) characterRepository.findAll();
   }

   public Optional<Character> findOne(Integer id) {
       return characterRepository.findById(id);
   }

   public Optional<Character> save(Character color) {
       return Optional.of(characterRepository.save(color));
   }

   public Optional<Character> update(Character entity) {
       Optional<Character> updatedEntity = Optional.empty();
       updatedEntity = characterRepository.findById(entity.getId());
       if (!updatedEntity.isEmpty())
           characterRepository.save(entity);
       return updatedEntity;
   }

   public Optional<Character> partialUpdate(Integer id, Map<Object, Object> fields) {
       try {
           Character entity = findOne(id).get();
           if (entity == null) {
               return Optional.empty();
           }
           Optional<Character> updatedEntity = Optional.empty();
           fields.forEach((updatedField, value) -> {
               Field field = ReflectionUtils.findField(Character.class, (String) updatedField);
               field.setAccessible(true);
               ReflectionUtils.setField(field, entity, value);
           });
           characterRepository.save(entity);
           updatedEntity = Optional.of(entity);
           return updatedEntity;
       } catch (Exception exception) {
           System.err.println(exception);
           return Optional.empty();
       }
   }

    public Boolean delete(Integer id) {
        boolean entity = characterRepository.existsById(id);
        if (entity) {
            characterRepository.deleteById(id);
        }
        return entity;
    }

   public void fillInicialData() {
       if (characterRepository.count() > 0)
           return;

       List<Character> inicial = new ArrayList<>();
       inicial.add(new Character("Activo"));
       inicial.add(new Character("Independiente"));
       inicial.add(new Character("Juguetón"));
       inicial.add(new Character("Protector"));
       inicial.add(new Character("Ruidoso"));
       inicial.add(new Character("Tímido"));
       inicial.add(new Character("Tranquilo"));
       inicial.add(new Character("Amoroso"));
       characterRepository.saveAll(inicial);
   }

}