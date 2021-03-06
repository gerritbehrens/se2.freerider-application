package de.freerider.restapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.freerider.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.freerider.datamodel.Customer;

@RestController
class CustomersController implements CustomersAPI {
    //
    private final ObjectMapper objectMapper;
    //
    private final HttpServletRequest request;
    @Autowired
    private CustomerRepository customerRepository = new CustomerRepository();

    CustomersController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;

    }

    @Override
    public ResponseEntity<List<?>> getCustomers() {
        //
        ResponseEntity<List<?>> re = null;
        System.err.println(request.getMethod() + " " + request.getRequestURI());
        try {
            ArrayNode arrayNode = objectMapper.createArrayNode();

            System.out.println(customerRepository.findAll());

            customerRepository.findAll().forEach(c -> {
                StringBuffer sb = new StringBuffer();
                sb.append(sb.length() == 0 ? "" : "; ").append(c.getContacts());
                arrayNode.add(
                        objectMapper.createObjectNode()
                                .put("id", c.getId())
                                .put("name", c.getLastName())
                                .put("first", c.getFirstName())
                                .put("contacts", sb.toString())
                );
            });

            ObjectReader reader = objectMapper.readerFor(new TypeReference<List<ObjectNode>>() {
            });
            List<String> list = reader.readValue(arrayNode);
            if (list.isEmpty()) {
                re = new ResponseEntity<List<?>>(HttpStatus.NOT_FOUND);
            } else {
                //
                re = new ResponseEntity<List<?>>(list, HttpStatus.OK);
            }

        } catch (IOException e) {
            re = new ResponseEntity<List<?>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
    }

    @Override
    public ResponseEntity<?> getCustomer(long id) {
        //
        ResponseEntity<String> re;
        System.err.println(request.getMethod() + " " + request.getRequestURI());
        try {
            ArrayNode arrayNode = findCustomerByID(id);
            String pretty = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
            //
            if (arrayNode.isEmpty()) {
                re = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            } else {
                re = new ResponseEntity<String>(pretty, HttpStatus.OK);
            }
        } catch (IOException e) {
            re = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return re;
    }

    //TOD
    //Pr??fung ob name und firstName vorhanden ist, dann BAD-REQUEST
    @Override
    public ResponseEntity<List<?>> postCustomers(Map<String, Object>[] jsonMap) {
        ArrayList<Map<String, Object>> err409Body = new ArrayList<>();
        ArrayList<Map<String, Object>> err400Body = new ArrayList<>();
ResponseEntity<List<?>> re = null;
        if (jsonMap == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//
        for (Map<String, Object> kvpairs : jsonMap) {
            kvpairs.keySet().forEach(key -> {
                Object value = kvpairs.get(key);
                System.out.println(" [ " + key + ", " + value + " ]");
            });
            Optional<Customer> co = accept(kvpairs);
            if (co.isEmpty()) {
                err400Body.add(kvpairs);
            } else {
                if (customerRepository.existsById(co.get().getId())) {
                    err409Body.add(kvpairs);
                } else {
                    customerRepository.save(co.get());
                }
            }

            if (!err409Body.isEmpty()) {
                return new ResponseEntity<>(err409Body, HttpStatus.CONFLICT);
            } else if (!err400Body.isEmpty()) {
                return new ResponseEntity<>(err400Body, HttpStatus.BAD_REQUEST);
            } else {
                re = new ResponseEntity<>(null, HttpStatus.CREATED);
            }
        }
        return re;
    }

private Optional<Customer> accept(Map<String, Object> kvpairs){
        Optional<Customer> co = Optional.empty();

        Long id = -1L;
        String lastName = jsonToString("name", kvpairs);
        String firstName = jsonToString("first", kvpairs);
        String contacts = jsonToString("contacts", kvpairs);

        if(kvpairs.containsKey("id")){
            Object o = kvpairs.get("id");

            if(o instanceof Long){
                id = (Long) o;
            }
            else if(o instanceof Integer){
                Integer i = (Integer) o;
                id = Long.valueOf(i.longValue());
            }
        } else{
            id = customerRepository.count() + 1L;
            while (customerRepository.existsById(id)){
                id++;
            }
        }

        //check conditions to create a customer
        if( id > 0 && !lastName.isEmpty() && !firstName.isEmpty()){
            Customer c = new Customer().setId(id).setName(firstName, lastName);
            if(!contacts.isEmpty()){
                String[] contactsArray = contacts.split(";");
                for(String contact : contactsArray){
                    c.addContact(contact);
                }
            }
            co = Optional.of(c);
        }
        return co;
}


private String jsonToString(String key, Map<String, Object> kvpairs) {
    String value = "";
    if (kvpairs.containsKey(key)) {
        Object o = kvpairs.get(key);
        if (o instanceof String) {
            value = o.toString().trim();
        }
    }
    return value;
}

    @Override
    public ResponseEntity<List<?>> putCustomers(Map<String, Object>[] jsonMap) {
        //TODO
        return null;
    }

    @Override
    public ResponseEntity<?> deleteCustomer(Long id) {
        // TODO Auto???generated method stub
        if (!findCustomerByID(id).isEmpty()) {
            customerRepository.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED); //202
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); //404
        }
    }

    private ArrayNode findCustomerByID(long id) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        Optional<Customer> c = customerRepository.findById(id);
        if (c.isPresent()) writeNode(c.get(), arrayNode);
        return arrayNode;
    }

    private void writeNode(de.freerider.datamodel.Customer c, ArrayNode arrayNode) {
        StringBuffer sb = new StringBuffer();
        c.getContacts().forEach(contact -> sb.append(sb.length() == 0 ? "" : "; ").append(contact));
        arrayNode.add(
                objectMapper.createObjectNode()
                        .put("id", c.getId())
                        .put("name", c.getLastName())
                        .put("first", c.getFirstName())
                        .put("contacts", sb.toString())
        );
    }
}