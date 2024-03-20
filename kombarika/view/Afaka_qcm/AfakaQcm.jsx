import React, {useState, useEffect} from "react";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

function Afaka Qcm(){
  const url = 'http://localhost:8080/';
  
  const [loading, setLoading] = useState(true);
  
  const [show, setShow] = useState(false);
  
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  
  const [show2, setShow2] = useState(false);
  
  const handleClose2 = () => setShow2(false);
  const handleShow2 = () => setShow2(true);
  
  const [error, setError] = useState(null);
  const [selectedItem, setSelectedItem] = useState(null);
  const handleSelectItem = (itemKey) => {
    handleShow2();
    const itemDetails = afakaQcm.find(item => item.idAs === itemKey);
    setSelectedItem(itemDetails);
  };

	const [afaka_qcm, setAfaka_qcm] = useState([]);
	
	const [qcmResult, setQcmResult] = useState([]);
	
	const [client, setClient] = useState([]);
	
	

//////// SAVE
  const handleSaveSubmit = async (event) => {
      event.preventDefault();
      const form = event.target;
      const formData = new FormData(form);
      const data = {};
  
      for (let [key, value] of formData.entries()) {
        if (form.elements[key].tagName === 'SELECT') {
          data[key] = { id: value };
        } else {
          data[key] = value;
        }
      }
  
      try {
        const response = await fetch(url + 'afakaQcm', {
          method: 'POST',
          body: JSON.stringify(data),
          headers: {
            'Content-Type': 'application/json'
          }
        });
  
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
  
        handleClose();
        // If you want to reload the page after success
        window.location.reload();
      } catch (error) {
        console.log('Error:', error);
      }
  };

//////// UPDATE
  const handleUpdateSubmit = async (event) => {
      event.preventDefault();
      const form = event.target;
      const formData = new FormData(form);
      const data = {};
      for (let [key, value] of formData.entries()) {
        if (form.elements[key].tagName === 'SELECT') {
          data[key] = { id: value };
        } else {
          data[key] = value;
        }
      }
      try {
        const response = await fetch(url + 'afakaQcm', {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
        });
  
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        handleClose2();
        // If you want to reload the page after success
        window.location.reload();
      } catch (error) {
        console.error('Error:', error);
      }
  };

//////// DELETE
  const handleDeleteClick = async (item) => {
    try {
      console.log(item);
      const response = await fetch(url + 'afakaQcm', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(item)
      });
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      // If you want to reload the page after success
      window.location.reload();
    } catch (error) {
      console.error('Error:', error);
    }
  };

	const handleInputIdAsChange = (event) => {
		setSelectedItem({ ...selectedItem, idAs: event.target.value });
	};
	
	const handleSelectQcmResultChange = (event) => {
		setSelectedItem({ ...selectedItem, qcmResult: event.target.value });
	};
	
	const handleSelectClientChange = (event) => {
		setSelectedItem({ ...selectedItem, client: event.target.value });
	};
	
	

	useEffect(() => {
		const getAfaka_qcm = async () => {
			try {
				const response = await fetch(url + 'afaka_qcm');
					if (!response.ok) {
						throw new Error('Network response was not ok');
					};
				const data = await response.json();
				setAfaka_qcm(data);
			} catch (error) {
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		getAfaka_qcm();
	}, []);
	useEffect(() => {
		const getQcmResult = async () => {
			try {
				const response = await fetch(url + 'qcmResult');
					if (!response.ok) {
						throw new Error('Network response was not ok');
					};
				const data = await response.json();
				setQcmResult(data);
			} catch (error) {
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		getQcmResult();
	}, []);
	useEffect(() => {
		const getClient = async () => {
			try {
				const response = await fetch(url + 'client');
					if (!response.ok) {
						throw new Error('Network response was not ok');
					};
				const data = await response.json();
				setClient(data);
			} catch (error) {
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		getClient();
	}, []);
	

  return (
    <>
      <div className="container">
          <div className="row justify-content-end">
              <div className="col" >   
                <div className="row">
                  <Button variant="primary" onClick={handleShow}>
                      Add Afaka Qcm
                  </Button>
                </div>    

                  <Modal show={show} onHide={handleClose}>
                      <Modal.Header closeButton>
                      <Modal.Title>Add Afaka Qcm</Modal.Title>
                      </Modal.Header>
                      <Modal.Body>
                          <form action="" method="" id="insert" onSubmit={handleSaveSubmit}>
	<div className="mb-3"> 
	 	<label className="form-label">idAs</label> 
	 	<select className="form-control" name="qcmResult" id="select-qcmResult">
			{region.map((elt) => (
				<option value={elt.idR}>{elt.}</option>
			))}
			
		</select>
	</div><div className="mb-3"> 
	 	<label className="form-label">idAs</label> 
	 	<select className="form-control" name="client" id="select-client">
			{region.map((elt) => (
				<option value={elt.idR}>{elt.}</option>
			))}
			
		</select>
	</div>
                              <div className="mb-3">
                                <Button variant="primary" type= "submit" >
                                  Save Changes
                                </Button>
                              </div>
                          </form>
                      </Modal.Body>
                      <Modal.Footer>
                      </Modal.Footer>
                  </Modal>
              </div>
              
          </div>
          <div className="row">
              <table className="table">
                  <thead id="table-head">
                      <tr>
			<th> Id As </th>
			<th> Qcm R </th>
			<th> Id Users </th>

                          <th></th>
                          <th></th>
                      </tr>
                  </thead>    
                  <tbody id="table-body">
                      {afakaQcm.map((item) => (
                              <tr key={item.idAs}>
		<td>{item.idAs}</td>
		<td>{item.qcmResult.}</td>
		<td>{item.client.}</td>

                              <td>
                                  <Button variant="danger" key={item.idAs} onClick={() => handleDeleteClick(item)}>
                                      Delete
                                  </Button>
                              </td>   
                              <td>
                                  <Button variant="warning" key={item.idAs} onClick={() => handleSelectItem(item.idAs)}>
                                      Update
                                  </Button>
                              </td>
                          </tr>
                      ))}
                  </tbody>
              </table>
              <Modal show={show2} onHide={handleClose2}>
                  <Modal.Header closeButton>
                      <Modal.Title>Update Afaka Qcm</Modal.Title>
                  </Modal.Header>
                  <Modal.Body>    
                      <form action="" method="" id="update" onSubmit={handleUpdateSubmit}>
	<div className="mb-3"> 
	 	<label className="form-label"></label> 
	 	<input className="form-control" type="hidden" name="idAs" onChange={handleInputIdAsChange} value={selectedItem ? selectedItem.idAs:''} />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label">idAs</label> 
	 	<select className="form-control" name="qcmResult">
			{region.map((elt) => (
		<option value={elt.idR}>{elt.}</option>
	))}
	
	{region.map((elt) => (
		<option value={elt.idR}>{elt.}</option>
	))}
	
	
	</select>
	</div><div className="mb-3"> 
	 	<label className="form-label">idAs</label> 
	 	<select className="form-control" name="client">
			{region.map((elt) => (
		<option value={elt.idR}>{elt.}</option>
	))}
	
	{region.map((elt) => (
		<option value={elt.idR}>{elt.}</option>
	))}
	
	
	</select>
	</div>
                          <div className="mb-3">
                            <Button variant="warning" type= "submit" >
                              Save Changes
                            </Button>
                          </div>
                      </form>  
                  </Modal.Body>
                  <Modal.Footer>

                  </Modal.Footer>
              </Modal>
          </div>
      </div>
    </>
  )
}

export default Afaka Qcm;
