package program.program.repository;


import lombok.Getter;

import org.springframework.stereotype.Repository;
import program.program.Model.Issuer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Getter
@Repository
public class IssuersRepository {

    List<Issuer> issuers = new ArrayList<>();

    public IssuersRepository(){

        String [] data = {"ADIN,2024-11-10,No\n",
                "ALK,2024-11-10,No\n",
                "ALKB,2024-11-10,No\n" ,
                "AMBR,2014-01-01,Yes\n" ,
                "AMEH,2024-11-10,No\n" ,
                "APTK,2024-11-10,No\n" ,
                "ATPP,2024-11-10,No\n" ,
                "AUMK,2024-11-10,No\n" ,
                "BANA,2024-11-10,No\n" ,
                "BGOR,2024-11-10,No\n" ,
                "BIKF,2024-11-10,No\n" ,
                "BIM,2024-11-10,No\n" ,
                "BLTU,2024-11-10,No\n" ,
                "CBNG,2024-11-10,No\n" ,
                "CDHV,2024-11-10,No\n" ,
                "CEVI,2024-11-10,No\n" ,
                "CKB,2024-11-10,No\n" ,
                "CKBKO,2024-11-10,No\n" ,
                "DEBA,2024-11-10,No\n" ,
                "DIMI,2024-11-10,No\n" ,
                "EDST,2024-11-10,No\n" ,
                "ELMA,2024-11-10,No\n" ,
                "ELNC,2024-11-10,No\n" ,
                "ENER,2024-11-10,No\n" ,
                "ENSA,2024-11-10,No\n" ,
                "EUHA,2024-11-10,No\n" ,
                "EUMK,2024-11-10,No\n" ,
                "EVRO,2024-11-10,No\n" ,
                "FAKM,2024-11-10,No\n" ,
                "FERS,2024-11-10,No\n" ,
                "FKTL,2024-11-10,No\n" ,
                "FROT,2024-11-10,No\n" ,
                "FUBT,2024-11-10,No\n" ,
                "GALE,2024-11-10,No\n" ,
                "GDKM,2024-11-10,No\n" ,
                "GECK,2024-11-10,No\n" ,
                "GECT,2024-11-10,No\n" ,
                "GIMS,2024-11-10,No\n" ,
                "GRDN,2024-11-10,No\n" ,
                "GRNT,2024-11-10,No\n" ,
                "GRSN,2024-11-10,No\n" ,
                "GRZD,2024-11-10,No\n" ,
                "GTC,2024-11-10,No\n" ,
                "GTRG,2024-11-10,No\n" ,
                "IJUG,2024-11-10,No\n" ,
                "INB,2024-11-10,No\n" ,
                "INHO,2024-11-10,No\n" ,
                "INOV,2024-11-10,No\n" ,
                "INPR,2024-11-10,No\n" ,
                "INTP,2024-11-10,No\n" ,
                "JAKO,2024-11-10,No\n" ,
                "JUSK,2024-11-10,No\n" ,
                "KARO,2024-11-10,No\n" ,
                "KDFO,2024-11-10,No\n" ,
                "KJUBI,2024-11-10,No\n" ,
                "KKST,2024-11-10,No\n" ,
                "KLST,2024-11-10,No\n" ,
                "KMB,2024-11-10,No\n" ,
                "KMPR,2024-11-10,No\n" ,
                "KOMU,2024-11-10,No\n" ,
                "KONF,2024-11-10,No\n" ,
                "KONZ,2024-11-10,No\n" ,
                "KORZ,2024-11-10,No\n" ,
                "KPSS,2024-11-10,No\n" ,
                "KULT,2024-11-10,No\n" ,
                "KVAS,2024-11-10,No\n" ,
                "LAJO,2024-11-10,No\n" ,
                "LHND,2024-11-10,No\n" ,
                "LOTO,2024-11-10,No\n" ,
                "LOZP,2024-11-10,No\n" ,
                "MAGP,2024-11-10,No\n" ,
                "MAKP,2024-11-10,No\n" ,
                "MAKS,2024-11-10,No\n" ,
                "MB,2024-11-10,No\n" ,
                "MERM,2024-11-10,No\n" ,
                "MKSD,2024-11-10,No\n" ,
                "MLKR,2024-11-10,No\n" ,
                "MODA,2024-11-10,No\n" ,
                "MPOL,2024-11-10,No\n" ,
                "MPT,2024-11-10,No\n" ,
                "MPTE,2024-11-10,No\n" ,
                "MTUR,2024-11-10,No\n" ,
                "MZHE,2024-11-10,No\n" ,
                "MZPU,2024-11-10,No\n" ,
                "NEME,2024-11-10,No\n" ,
                "NOSK,2024-11-10,No\n" ,
                "OBPP,2024-11-10,No\n" ,
                "OILK,2024-11-10,No\n" ,
                "OKTA,2024-11-10,No\n" ,
                "OMOS,2024-11-10,No\n" ,
                "OPFO,2024-11-10,No\n" ,
                "OPTK,2024-11-10,No\n" ,
                "ORAN,2024-11-10,No\n" ,
                "OSPO,2024-11-10,No\n" ,
                "OTEK,2024-11-10,No\n" ,
                "PELK,2024-11-10,No\n" ,
                "PGGV,2024-11-10,No\n" ,
                "PKB,2024-11-10,No\n" ,
                "POPK,2024-11-10,No\n" ,
                "PPIV,2024-11-10,No\n" ,
                "PROD,2024-11-10,No\n" ,
                "PROT,2024-11-10,No\n" ,
                "PTRS,2024-11-10,No\n" ,
                "RADE,2024-11-10,No\n" ,
                "REPL,2024-11-10,No\n" ,
                "RIMI,2024-11-10,No\n" ,
                "RINS,2024-11-10,No\n" ,
                "RZEK,2024-11-10,No\n" ,
                "RZIT,2024-11-10,No\n" ,
                "RZIZ,2024-11-10,No\n" ,
                "RZLE,2024-11-10,No\n" ,
                "RZLV,2024-11-10,No\n" ,
                "RZTK,2024-11-10,No\n" ,
                "RZUG,2024-11-10,No\n" ,
                "RZUS,2024-11-10,No\n" ,
                "SBT,2024-11-10,No\n" ,
                "SDOM,2024-11-10,No\n" ,
                "SIL,2024-11-10,No\n" ,
                "SKON,2024-11-10,No\n" ,
                "SKP,2024-11-10,No\n" ,
                "SLAV,2024-11-10,No\n" ,
                "SNBT,2024-11-10,No\n" ,
                "SNBTO,2024-11-10,No\n" ,
                "SOLN,2024-11-10,No\n" ,
                "SPAZ,2024-11-10,No\n" ,
                "SPAZP,2024-11-10,No\n" ,
                "SPOL,2024-11-10,No\n" ,
                "SSPR,2024-11-10,No\n" ,
                "STB,2024-11-10,No\n" ,
                "STBP,2024-11-10,No\n" ,
                "STIL,2024-11-10,No\n" ,
                "STOK,2024-11-10,No\n" ,
                "TAJM,2024-11-10,No\n" ,
                "TBKO,2024-11-10,No\n" ,
                "TEAL,2024-11-10,No\n" ,
                "TEHN,2024-11-10,No\n" ,
                "TEL,2024-11-10,No\n" ,
                "TETE,2024-11-10,No\n" ,
                "TIKV,2024-11-10,No\n" ,
                "TKPR,2024-11-10,No\n" ,
                "TKVS,2024-11-10,No\n" ,
                "TNB,2024-11-10,No\n" ,
                "TRDB,2024-11-10,No\n" ,
                "TRPS,2024-11-10,No\n" ,
                "TRUB,2024-11-10,No\n" ,
                "TSMP,2024-11-10,No\n" ,
                "TSZS,2024-11-10,No\n" ,
                "TTK,2024-11-10,No\n" ,
                "TTKO,2024-11-10,No\n" ,
                "UNI,2024-11-10,No\n" ,
                "USJE,2024-11-10,No\n" ,
                "VARG,2024-11-10,No\n" ,
                "VFPM,2024-11-10,No\n" ,
                "VITA,2024-11-10,No\n" ,
                "VROS,2024-11-10,No\n" ,
                "VSC,2024-11-10,No\n" ,
                "VTKS,2024-11-10,No\n" ,
                "ZAS,2024-11-10,No\n" ,
                "ZILU,2024-11-10,No\n" ,
                "ZILUP,2024-11-10,No\n" ,
                "ZIMS,2024-11-10,No\n" ,
                "ZKAR,2024-11-10,No\n" ,
                "ZPKO,2024-11-10,No\n" ,
                "ZPOG,2024-11-10,No\n" ,
                "ZUAS,2024-11-10,No"};

                for(int i=0;i<data.length;i++){
                    String line = data[i];
                    String [] parts = line.split(",");
                    issuers.add(new Issuer(parts[0], parts[1], i));
                }


    }


    public Issuer getByName(String name){
        Issuer issu = new Issuer("No such issuer", "0000-00-00", -1);
        for (Issuer issuer : issuers) {
            if (Objects.equals(issuer.getName(), name)) {
                return issuer;
            }
        }
        return issu;
    }

    public List<Issuer> listAll(){
        return new ArrayList<>(issuers);
    }

    public Issuer getById(int id){
        for (Issuer issuer:issuers){
            if(issuer.getID()==id){
                return issuer;
            }
        }
        return null;
    }
}
