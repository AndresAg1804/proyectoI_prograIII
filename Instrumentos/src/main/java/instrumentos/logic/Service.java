package instrumentos.logic;

import instrumentos.data.Data;
import instrumentos.data.XmlPersister;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Service {
    private static Service theInstance;

    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    private Data data;

    private Service(){
        try{
            data = XmlPersister.instance().load();
        }
        catch (Exception e){
            data = new Data();
        }

    }

    public void stop(){
        try{
            XmlPersister.instance().store(data);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //================= TIPOS DE INSTRUMENTO ============
    public void create(TipoInstrumento e) throws Exception{
        TipoInstrumento result = data.getTipos().stream()
                .filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
        if (result==null) data.getTipos().add(e);
        else throw new Exception("Tipo ya existe");
    }

    public TipoInstrumento read(TipoInstrumento e) throws Exception{
        TipoInstrumento result = data.getTipos().stream()
                .filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
        if (result!=null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void update(TipoInstrumento e) throws Exception{
        TipoInstrumento result = null;
        try{
            result = this.read(e);
            data.getTipos().remove(result);
            data.getTipos().add(e);
        }catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void delete(TipoInstrumento e) throws Exception{
        data.getTipos().remove(e);
     }

    public List<TipoInstrumento> search(TipoInstrumento e){
        return data.getTipos().stream()
                .filter(i->i.getNombre().contains(e.getNombre()))
                .sorted(Comparator.comparing(TipoInstrumento::getNombre))
                .collect(Collectors.toList());
    }

    //================= INSTRUMENTOS ============

    public long validateDelete(TipoInstrumento e){
        return data.getInstrumentos().stream()
                .filter(i->i.getTipo().getCodigo().equals(e.getCodigo())).count();
    }
    public void create(Instrumento e) throws Exception{
        Instrumento result = data.getInstrumentos().stream()
                .filter(i->i.getSerie().equals(e.getSerie())).findFirst().orElse(null);
        if (result==null) data.getInstrumentos().add(e);
        else throw new Exception("Tipo ya existe");
    }

    public Instrumento read(Instrumento e) throws Exception{
        Instrumento result = data.getInstrumentos().stream()
                .filter(i->i.getSerie().equals(e.getSerie())).findFirst().orElse(null);
        if (result!=null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void update(Instrumento e) throws Exception{
        Instrumento result = null;
        try{
            result = this.read(e);
            data.getInstrumentos().remove(result);
            data.getInstrumentos().add(e);
        }catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void delete(Instrumento e) throws Exception{
        data.getInstrumentos().remove(e);
    }

    public List<Instrumento> search(Instrumento e){
        return data.getInstrumentos().stream()
                .filter(i->i.getDescripcion().contains(e.getDescripcion()))
                .sorted(Comparator.comparing(Instrumento::getDescripcion))
                .collect(Collectors.toList());
    }

    //================= Calibraciones ============

    public void create(Instrumento instru, Calibraciones e) throws Exception {
        int num;
        num=instru.getCalibraciones().size()+1;
        e.setNumero(String.valueOf(num));
        instru.getCalibraciones().add(e);

    }

    public Calibraciones read(Instrumento instru, Calibraciones e) throws Exception{
        Calibraciones result = instru.getCalibraciones().stream()
                .filter(i->i.getNumero()==(e.getNumero())).findFirst().orElse(null);
        if (result!=null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void update(Instrumento instru, Calibraciones e) throws Exception{
        Calibraciones result = null;
        try{
            result = this.read(instru, e);
            instru.getCalibraciones().remove(result);
            instru.getCalibraciones().add(e);
        }catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void delete(Instrumento instr, Calibraciones e) throws Exception{
        instr.getCalibraciones().remove(e);
    }

    public List<Calibraciones> search(Instrumento instru, Calibraciones e){
        if(instru == null){
            Instrumento ins = new Instrumento();
            return ins.getCalibraciones();
        }
        return instru.getCalibraciones().stream()
                .filter(i->i.getNumero().equals(e.getNumero()))
                .sorted(Comparator.comparing(Calibraciones::getNumero))
                .collect(Collectors.toList());
    }

    public List<Calibraciones> search2(Instrumento instru, Calibraciones e){
        if(instru == null){
            Instrumento ins = new Instrumento();
            return ins.getCalibraciones();
        }
        return instru.getCalibraciones().stream()
                .filter(i->i.getNumero()!=(e.getNumero()))
                .sorted(Comparator.comparing(Calibraciones::getNumero))
                .collect(Collectors.toList());
    }


}
