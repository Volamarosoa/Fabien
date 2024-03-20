<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <p>Coucou</p>
    <a href="MatiereController/find-matiere.do">Liste Matiere</a>
    <form action="MatiereController/find-matiere.do" method="get">
        <input type="text" id="id" name="id" placeholder="ID Matiere">
        <input type="text" id="matiere" name="matiere" placeholder="Nom Matiere">
        <button type="submit">Valider</button>
    </form>
    
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    function update() {
        $.ajax({
            url: "MatiereController/delete-matiere.do",
            method: "DELETE",
            dataType: "json",
            data: { id: document.getElementById("id"), matiere: document.getElementById("matiere") },
            success: function(data) {
                console.log(data);
            },
            error: function(xhr, status, error) {
                console.error("Erreur AJAX:", status, error);
            }
        });
    }
</script>
</body>

</html>