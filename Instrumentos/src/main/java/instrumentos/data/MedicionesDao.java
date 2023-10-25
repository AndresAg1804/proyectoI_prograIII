package instrumentos.data;


import instrumentos.logic.Instrumento;
import instrumentos.logic.Mediciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicionesDao {
    Database db;

    public MedicionesDao() {
        db = Database.instance();
    }

    public int create(Mediciones e) throws Exception {
        String sql = "insert into " +
                "Mediciones " +
                "(referencia, lectura, calibracion_id) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getReferencia());
        stm.setString(2, String.valueOf(e.getLectura()));
        stm.setString(3, String.valueOf(e.getCalibraciones().getNumero()));

        ResultSet keys = db.executeUpdateWithKeys(stm);
        keys.next();
        return keys.getInt(1);
    }

    public Mediciones read(String serie) throws Exception {
        String sql = "select * from " +
                "Mediciones i " +
                "where i.serie=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, serie);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "i");
        } else {
            throw new Exception("Medicion NO EXISTE");
        }
    }

    public void update(Mediciones e) throws Exception {
        String sql = "update " +
                "Mediciones " +
                "set referencia=?" +
                "where medida=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getReferencia());
        stm.setString(2, e.getMedida());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicion NO EXISTE");
        }

    }

    public void delete(Mediciones e) throws Exception {
        String sql = "delete " +
                "from Mediciones " +
                "where medida=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getMedida());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Medicion NO EXISTE");
        }
    }

    public List<Mediciones> search(Mediciones e) throws Exception {
        List<Mediciones> resultado = new ArrayList<Mediciones>();
        String sql = "select * from " +
                "Mediciones i " +
                "where i.medida like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getMedida() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "i"));
        }
        return resultado;
    }

    public Mediciones from(ResultSet rs, String alias) throws Exception {
        CalibracionesDao ed = new CalibracionesDao();
        Mediciones e = new Mediciones();
        e.setMedida(rs.getString(alias + ".medida"));
        //e.setCalibraciones(ed.from(rs,"t"));
        e.setReferencia(rs.getString(alias + ".referencia"));
        e.setLectura(Integer.parseInt(rs.getString(alias+".lectura")));
        return e;
    }
}
