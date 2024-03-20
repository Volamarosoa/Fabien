import React, {useState, useEffect} from "react";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

function Avance(){
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
    const itemDetails = avance.find(item => item.id === itemKey);
    setSelectedItem(itemDetails);
  };

	const [avance, setAvance] = useState([]);
	
	const [employer, setEmployer] = useState([]);
	
	

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
        const response = await fetch(url + 'avance', {
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
        const response = await fetch(url + 'avance', {
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
      const response = await fetch(url + 'avance', {
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

	const handleInputDateChange = (event) => {
		setSelectedItem({ ...selectedItem, date: event.target.value });
	};
	
	const handleInputAvanceChange = (event) => {
		setSelectedItem({ ...selectedItem, avance: event.target.value });
	};
	
	const handleInputIdChange = (event) => {
		setSelectedItem({ ...selectedItem, id: event.target.value });
	};
	
	const handleSelectEmployerChange = (event) => {
		setSelectedItem({ ...selectedItem, employer: event.target.value });
	};
	
	

	useEffect(() => {
		const getAvance = async () => {
			try {
				const response = await fetch(url + 'avance');
					if (!response.ok) {
						throw new Error('Network response was not ok');
					};
				const data = await response.json();
				setAvance(data);
			} catch (error) {
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		getAvance();
	}, []);
	useEffect(() => {
		const getEmployer = async () => {
			try {
				const response = await fetch(url + 'employer');
					if (!response.ok) {
						throw new Error('Network response was not ok');
					};
				const data = await response.json();
				setEmployer(data);
			} catch (error) {
				setError(error);
			} finally {
				setLoading(false);
			}
		};
		getEmployer();
	}, []);
	

  return (
    <>
      <div className="container">
          <div className="row justify-content-end">
              <div className="col" >   
                <div className="row">
                  <Button variant="primary" onClick={handleShow}>
                      Add Avance
                  </Button>
                </div>    

                  <Modal show={show} onHide={handleClose}>
                      <Modal.Header closeButton>
                      <Modal.Title>Add Avance</Modal.Title>
                      </Modal.Header>
                      <Modal.Body>
                          <form action="" method="" id="insert" onSubmit={handleSaveSubmit}>
	<div className="mb-3"> 
	 	<label className="form-label">Date</label> 
	 	<input className="form-control" type="Date" name="date" />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label">Avance</label> 
	 	<input className="form-control" type="number" name="avance" />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label">id</label> 
	 	<select className="form-control" name="employer" id="select-employer">
			{region.map((elt) => (
				<option value={elt.idEmp}>{elt.adresse}</option>
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
			<th> Date </th>
			<th> Avance </th>
			<th> Id </th>
			<th> Id Employe </th>

                          <th></th>
                          <th></th>
                      </tr>
                  </thead>    
                  <tbody id="table-body">
                      {avance.map((item) => (
                              <tr key={item.id}>
		<td>{item.date}</td>
		<td>{item.avance}</td>
		<td>{item.id}</td>
		<td>{item.employer.adresse}</td>

                              <td>
                                  <Button variant="danger" key={item.id} onClick={() => handleDeleteClick(item)}>
                                      Delete
                                  </Button>
                              </td>   
                              <td>
                                  <Button variant="warning" key={item.id} onClick={() => handleSelectItem(item.id)}>
                                      Update
                                  </Button>
                              </td>
                          </tr>
                      ))}
                  </tbody>
              </table>
              <Modal show={show2} onHide={handleClose2}>
                  <Modal.Header closeButton>
                      <Modal.Title>Update Avance</Modal.Title>
                  </Modal.Header>
                  <Modal.Body>    
                      <form action="" method="" id="update" onSubmit={handleUpdateSubmit}>
	<div className="mb-3"> 
	 	<label className="form-label">Date</label> 
	 	<input className="form-control" type="Date" name="date" onChange={handleInputDateChange} value={selectedItem ? selectedItem.date:''} />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label">Avance</label> 
	 	<input className="form-control" type="number" name="avance" onChange={handleInputAvanceChange} value={selectedItem ? selectedItem.avance:''} />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label"></label> 
	 	<input className="form-control" type="hidden" name="id" onChange={handleInputIdChange} value={selectedItem ? selectedItem.id:''} />
	</div>
	<div className="mb-3"> 
	 	<label className="form-label">id</label> 
	 	<select className="form-control" name="employer">
			{region.map((elt) => (
		<option value={elt.idEmp}>{elt.adresse}</option>
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

export default Avance;
