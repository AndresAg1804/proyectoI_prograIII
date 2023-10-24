package instrumentos.data;

import instrumentos.logic.TipoInstrumento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TipoInstrumentoDao {
    Database db;

    public TipoInstrumentoDao() {
        db = Database.instance();
    }

    public void create(TipoInstrumento e) throws Exception {
        String sql = "insert into " +
                "TipoInstrumento " +
                "(codigo, nombre, unidad) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        stm.setString(2, e.getNombre());
        stm.setString(3, e.getUnidad());

        db.executeUpdate(stm);
    }

    public TipoInstrumento read(String codigo) throws Exception {
        String sql = "select " +
                "* " +
                "from  TipoInstrumento t " +
                "where t.codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, codigo);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "t");
        } else {
            throw new Exception("TIPO INSTRUMENTO NO EXISTE");
        }
    }

    public void update(TipoInstrumento e) throws Exception {
        String sql = "update " +
                "TipoInstrumento " +
                "set nombre=?, unidad=? " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getNombre());
        stm.setString(2, e.getUnidad());
        stm.setString(3, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("TIPO DE INSTRUMENTO NO EXISTE");
        }

    }

    public void delete(TipoInstrumento e) throws Exception {
        String sql = "delete " +
                "from TipoInstrumento " +
                "where codigo=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getCodigo());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("IPO DE INSTRUMENTO NO EXISTE");
        }
    }

    public List<TipoInstrumento> search(TipoInstrumento e) throws Exception {
        List<TipoInstrumento> resultado = new ArrayList<TipoInstrumento>();
        String sql = "select * " +
                "from " +
                "TipoInstrumento t " +
                "where t.nombre like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getNombre() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "t"));
        }
        return resultado;
    }

    public TipoInstrumento from(ResultSet rs, String alias) throws Exception {
        TipoInstrumento e = new TipoInstrumento();
        e.setCodigo(rs.getString(alias + ".codigo"));
        e.setNombre(rs.getString(alias + ".nombre"));
        e.setUnidad(rs.getString(alias + ".unidad"));
        return e;
    }

}
