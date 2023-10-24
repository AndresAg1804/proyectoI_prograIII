package instrumentos.data;

import instrumentos.logic.Calibraciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CalibracionesDao {
    Database db;

    public CalibracionesDao() {
        db = Database.instance();
    }

    public void create(Calibraciones e) throws Exception {
        String sql = "insert into " +
                "Calibraciones " +
                "(id, fecha, mediciones, instrumento_serie) " +
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNumero());
        stm.setString(2, e.getFecha());
        stm.setString(3, String.valueOf(e.getMediciones()));
        stm.setString(4, String.valueOf(e.getInstrumento().getSerie()));

        db.executeUpdate(stm);
    }

    public Calibraciones read(String id) throws Exception {
        String sql = "select * from " +
                "Calibraciones i inner join Instrumento t on i.instrumento_serie=t.serie " +
                "where i.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "i");
        } else {
            throw new Exception("Calibracion NO EXISTE");
        }
    }

    public void update(Calibraciones e) throws Exception {
        String sql = "update " +
                "Calibraciones " +
                "set mediciones=?" +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, String.valueOf(e.getMediciones()));
        stm.setString(2, e.getNumero());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Calibracion NO EXISTE");
        }

    }

    public void delete(Calibraciones e) throws Exception {
        String sql = "delete " +
                "from Calibraciones " +
                "where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNumero());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("Calibracion NO EXISTE");
        }
    }

    public List<Calibraciones> search(Calibraciones e) throws Exception {
        List<Calibraciones> resultado = new ArrayList<Calibraciones>();
        String sql = "select * from " +
                "Calibraciones i inner join Instrumento t on i.instrumento_serie=t.serie " +
                "where i.id like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNumero() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "i"));
        }
        return resultado;
    }

    public Calibraciones from(ResultSet rs, String alias) throws Exception {
        InstrumentoDao ed = new InstrumentoDao();
        Calibraciones e = new Calibraciones();
        e.setNumero(rs.getString(alias + ".id"));
        e.setFecha(rs.getString(alias + ".fecha"));
        e.setMediciones(Integer.parseInt(rs.getString(alias + ".mediciones")));
        e.setInstrumento(ed.from(rs,"t"));
        return e;
    }

}