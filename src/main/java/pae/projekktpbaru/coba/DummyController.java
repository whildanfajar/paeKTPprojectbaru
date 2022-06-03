/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pae.projekktpbaru.coba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author whildan fajar
 */
@Controller
public class DummyController {
    
    DummyJpaController dummyController = new DummyJpaController();
    List<Dummy> data = new ArrayList<>();
    
    @RequestMapping("/read")
    public String getDummy(Model model){
        try{
            data = dummyController.findDummyEntities();
            
        }catch (Exception e) {
            
        }
        model.addAttribute("data", data);
        return "dummy";
    }
    
    @RequestMapping("/create")
    public String createDummy(){
        
        return "dummy/create";
    
    }
    
    @PostMapping(value = "/newdata", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String newDummyData(@RequestParam("gambar") MultipartFile file, HttpServletRequest data, HttpServletResponse respon)
        throws ParseException, Exception{
        
       Dummy dumdata = new Dummy();
            
       int id = Integer.parseInt(data.getParameter("id"));
       Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
       byte[] img = file.getBytes();
       dumdata.setId(id);
       dumdata.setTanggal(date);
       dumdata.setGambar(img);
       
       dummyController.create(dumdata);
       respon.sendRedirect("/read");
       return "created";
    
    }
    
      @RequestMapping(value = "/img", method = RequestMethod.GET, produces = {
        MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<byte[]> getImage(@RequestParam("id") int id) throws Exception {
    Dummy dumdata = dummyController.findDummy(id);
    byte[] image = dumdata.getGambar();
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
    }
    
    @RequestMapping("/edit/{id}")
    public String updateDummy(@PathVariable("id") int id, Model m) throws Exception {
        Dummy dumdata = dummyController.findDummy(id);
        m.addAttribute("data", dumdata);
        return "dummy/update";
    }
    
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public String updateDummy(@RequestParam("gambar") MultipartFile file, HttpServletRequest data, HttpServletResponse respon)
      throws ParseException, Exception {
    Dummy dumdata = new Dummy();

    int id = Integer.parseInt(data.getParameter("id"));
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.getParameter("tanggal"));
    byte[] image = file.getBytes();
    dumdata.setId(id);
    dumdata.setTanggal(date);
    dumdata.setGambar(image);

    dummyController.edit(dumdata);
    respon.sendRedirect("/read");
    return "updated";
  }
    
    @GetMapping("/delete/{id}")
    @ResponseBody
    public String deleteDummy(@PathVariable("id") int id, HttpServletResponse respon) throws Exception {
        dummyController.destroy(id);
        respon.sendRedirect("/read");
        return "deleted";
    }
}
