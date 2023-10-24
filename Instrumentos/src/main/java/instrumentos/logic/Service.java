package instrumentos.logic;

import instrumentos.data.Data;
import instrumentos.data.XmlPersister;
import instrumentos.data.TipoInstrumentoDao;
import instrumentos.data.InstrumentoDao;
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
    private TipoInstrumentoDao tipoInstrumentoDao;
    private InstrumentoDao instrumentoDao;

    private Service(){
        try{
            data = XmlPersister.instance().load();
            tipoInstrumentoDao = new TipoInstrumentoDao();
            instrumentoDao = new InstrumentoDao();
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
    public void create(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.create(e);
    }

    public TipoInstrumento read(TipoInstrumento e) throws Exception{
        return tipoInstrumentoDao.read(e.getCodigo());
    }

    public void update(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.update(e);
    }

    public void delete(TipoInstrumento e)throws Exception{
        tipoInstrumentoDao.delete(e);
    }

    public List<TipoInstrumento> search(TipoInstrumento e) throws Exception {
        return tipoInstrumentoDao.search(e);
    }

    // ------------ INSTRUMENTOS -------------
    public void create(Instrumento e) throws Exception{
        instrumentoDao.create(e);
    }

    public Instrumento read(Instrumento e) throws Exception{
        return instrumentoDao.read(e.getSerie());
    }

    public void update(Instrumento e)throws Exception{
        instrumentoDao.update(e);
    }

    public void delete(Instrumento e)throws Exception{
        instrumentoDao.delete(e);
    }

    public List<Instrumento> search(Instrumento v) throws Exception {
        return instrumentoDao.search(v);
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
