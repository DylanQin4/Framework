SELECT
    v.id AS id,
    v.date_vol AS dateVol,
    vd.label AS depart,
    va.label AS arrive,
    a.nom AS avion,
    v.statue AS statue,
    COALESCE(pe.prix, 0) AS prixEco,
    COALESCE(pb.prix, 0) AS prixBuss
FROM vol v
         JOIN ville vd ON v.ville_depart_id = vd.id
         JOIN ville va ON v.ville_arrive_id = va.id
         JOIN avion a ON v.avion_id = a.id
         LEFT JOIN prix_siege pe ON v.id = pe.vol_id AND pe.type_siege_id = 1 -- Classe économique
         LEFT JOIN prix_siege pb ON v.id = pb.vol_id AND pb.type_siege_id = 2 -- Classe business
ORDER BY v.date_vol DESC;
