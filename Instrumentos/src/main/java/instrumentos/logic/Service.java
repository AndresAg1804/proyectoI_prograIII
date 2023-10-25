/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package instrumentos.logic;

import instrumentos.data.CalibracionesDao;
import instrumentos.data.TipoInstrumentoDao;
import instrumentos.data.InstrumentoDao;
import instrumentos.data.MedicionesDao;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private static Service theInstance;
    public static Service instance(){
        if (theInstance==null){
            theInstance=new Service();
        }
        return theInstance;
    }
    private TipoInstrumentoDao tipoInstrumentoDao;
    private InstrumentoDao instrumentoDao;
    private CalibracionesDao calibracionesDao;
    private MedicionesDao medicionesDao;

    public Service() {
        try{
            tipoInstrumentoDao = new TipoInstrumentoDao();
            instrumentoDao = new InstrumentoDao();
            calibracionesDao = new CalibracionesDao();
            medicionesDao = new MedicionesDao();
            //calibra = new ArrayList<>();
        }
        catch(Exception e){
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

    public void create(Calibraciones e) throws Exception{
        int id = calibracionesDao.create(e);
        e.setNumero(String.valueOf(id));
        e.getMedicionesList().forEach((med) -> {   // for each medicion in calibracion
            try {
               int medida =  medicionesDao.create(med);
               med.setMedida(String.valueOf(medida));
            } catch (Exception ex) {
            }
        });
    }

    public Calibraciones read(Calibraciones e) throws Exception{
        return calibracionesDao.read(e.getNumero());
    }

    public void update(Calibraciones e)throws Exception{
        calibracionesDao.update(e);
    }

    public void delete(Calibraciones e)throws Exception{
        calibracionesDao.delete(e);
    }

    public List<Calibraciones> search(Calibraciones v) throws Exception {
        return calibracionesDao.search(v);
    }

    //================= Mediciones ============

    public void create(Mediciones e) throws Exception{
        medicionesDao.create(e);
    }

    public Mediciones read(Mediciones e) throws Exception{
        return medicionesDao.read(e.getMedida());
    }

    public void update(Mediciones e)throws Exception{
        medicionesDao.update(e);
    }

    public void delete(Mediciones e)throws Exception{
        medicionesDao.delete(e);
    }

    public List<Mediciones> search(Mediciones v) throws Exception {
        return medicionesDao.search(v);
    }

    public List<Mediciones> searchMedicionesByCalibracion(String id){
        List<Mediciones> med = new ArrayList<>();
        med = medicionesDao.searchMedicionesByCalibracion(id);
        return med;
    }

    public List<Calibraciones> searchCalibracionesByInstrumento(String serie){
        List<Calibraciones> cali = new ArrayList<>();
        cali = calibracionesDao.searchCalibracionesByInstrumento(serie);
        for(Calibraciones c : cali){
            c.setMedicionesList(searchMedicionesByCalibracion(c.getNumero()));
        }
        return cali;
    }
}
