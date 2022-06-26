package rest;
import model.Zbor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.RepoZbor;
import repository.RepositoryException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/zbor")
public class ZborController {

    private static final String template = "Hello, %s!";

    @Autowired
    private RepoZbor zborRepository;
/*
    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }



 */
    @RequestMapping( method=RequestMethod.GET)
    public Zbor[] getAll(){
        System.out.println("Get all users ...");
        List<Zbor> result =
                StreamSupport.stream(zborRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());
        return result.toArray(new Zbor[0]);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("Get by id "+id);
        Zbor zbor=zborRepository.findOne(Long.valueOf(id));
        if (zbor==null)
            return new ResponseEntity<String>("User not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Zbor>(zbor, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Zbor create(@RequestBody Zbor zbor){
        //LocalDateTime l = LocalDateTime.now();
        //zbor.setDataOra(l);
        zborRepository.save(zbor);

            return zbor;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Zbor update(@RequestBody Zbor zbor) {
            System.out.println("Updating user ...");
             zborRepository.update(zbor.getId(),zbor);
             return zbor;

    }
   // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String id){
        System.out.println("Deleting user ... "+id);
        try {
            zborRepository.delete(Long.valueOf(id));
            return new ResponseEntity<Zbor>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

/*
    @RequestMapping("/{user}/name")
    public String name(@PathVariable String user){
        User result=userRepository.findBy(user);
        System.out.println("Result ..."+result);

        return result.getName();
    }



 */

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
