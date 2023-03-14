document.getElementById("file").addEventListener("change", uploadResource);
document.getElementById("remove").addEventListener("click", removeResource);

function uploadResource() {
  const file = document.getElementById("file");
  const formData = new FormData();
  formData.append("file", file.files[0]);

  fetch("/s3/resource", {
    method: "POST",
    body: formData
  })
    .then(result => result.json())
    .then(data => {
      console.log(data);
      document.getElementById("img").setAttribute("src", data.path);
      document.getElementById("remove").setAttribute("key", data.key);
    })
    .catch(error => console.log(`error => ${error}`));
}

function removeResource() {
  const key = document.getElementById("remove").getAttribute("key");
  if (!key) {
    return;
  }
  const formData = new FormData();
  formData.append("key", key);

  fetch("/s3/resource", {
    method: "DELETE",
    body: formData
  })
    .then(result => {
      if (result.ok && result.status === 200) {
        alert("해당 이미지가 삭제되었습니다.");
        document.getElementById("img").removeAttribute("src");
      }
    })
    .catch(error => console.log(`error => ${error}`));
}