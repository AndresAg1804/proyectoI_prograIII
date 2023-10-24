package instrumentos.data;

import instrumentos.logic.Instrumento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InstrumentoDao {
    Database db;

    public InstrumentoDao() {
        db = Database.instance();
    }

    public void create(Instrumento e) throws Exception {
        String sql = "insert into " +
                "Instrumento " +
                "(serie, tipo, descripcion, minimo, maximo, tolerancia) " +
                "values(?,?,?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getSerie());
        stm.setString(2, e.getTipo().getCodigo());
        stm.setString(3, e.getDescripcion());
        stm.setString(4, String.valueOf(e.getMinimo()));
        stm.setString(5, String.valueOf(e.getMaximo()));
        stm.setString(6, String.valueOf(e.getTolerancia()));

        db.executeUpdate(stm);
    }

    public Instrumento read(String serie) throws Exception {
        String sql = "select * from " +
                "instrumento i inner join tipoinstrumento t on i.tipo=t.codigo " +
                "where i.serie=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, serie);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "i");
        } else {
            throw new Exception("INSTRUMENTO NO EXISTE");
        }
    }

    public void update(Instrumento e) throws Exception {
        String sql = "update " +
                "Instrumento " +
                "set tipo=?, descripcion=?, set minimo=?, set maximo=?, set tolerancia=?" +
                "where serie=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getTipo().getCodigo());
        stm.setString(2, e.getDescripcion());
        stm.setString(3, String.valueOf(e.getMinimo()));
        stm.setString(4, String.valueOf(e.getMaximo()));
        stm.setString(5, String.valueOf(e.getTolerancia()));
        stm.setString(6, e.getSerie());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("INSTRUMENTO NO EXISTE");
        }

    }

    public void delete(Instrumento e) throws Exception {
        String sql = "delete " +
                "from Instrumento " +
                "where serie=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getSerie());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("IPO DE INSTRUMENTO NO EXISTE");
        }
    }

    public List<Instrumento> search(Instrumento e) throws Exception {
        List<Instrumento> resultado = new ArrayList<Instrumento>();
        String sql = "select * from " +
                "instrumento i inner join tipoInstrumento t on i.tipo=t.codigo " +
                "where i.descripcion like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + e.getDescripcion() + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "i"));
        }
        return resultado;
    }

    public Instrumento from(ResultSet rs, String alias) throws Exception {
        TipoInstrumentoDao ed = new TipoInstrumentoDao();
        Instrumento e = new Instrumento();
        e.setSerie(rs.getString(alias + ".serie"));
        e.setTipo(ed.from(rs,"t"));
        e.setDescripcion(rs.getString(alias + ".descripcion"));
        e.setMinimo(Integer.parseInt(rs.getString(alias+".minimo")));
        e.setMaximo(Integer.parseInt(rs.getString(alias+".maximo")));
        e.setTolerancia(Integer.parseInt(rs.getString(alias+".tolerancia")));
        return e;
    }

}