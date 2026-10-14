// Java Exception & File I/O Simulation
let contacts = [
  { id: 'C-101', name: 'Emma Watson', email: 'emma.w@example.com', phone: '+1-555-0192', category: 'Work' },
  { id: 'C-102', name: 'Liam Neeson', email: 'liam.n@example.com', phone: '+1-555-0144', category: 'Personal' },
  { id: 'C-103', name: 'Sophia Chen', email: 'sophia.c@example.com', phone: '+1-555-0188', category: 'Family' },
  { id: 'C-104', name: 'Marcus Brody', email: 'marcus.b@example.com', phone: '+1-555-0155', category: 'Work' }
];

let activeFilter = 'all';
let searchQuery = '';

// DOM Elements
const contactsGrid = document.getElementById('contactsGrid');
const consoleLogs = document.getElementById('consoleLogs');
const searchInput = document.getElementById('searchInput');
const contactModal = document.getElementById('contactModal');
const contactForm = document.getElementById('contactForm');
const modalError = document.getElementById('modalError');
const modalTitle = document.getElementById('modalTitle');

function logEvent(message, type = 'info') {
  const entry = document.createElement('div');
  entry.className = `log-entry ${type}`;
  const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
  entry.textContent = `[${time}] ${message}`;
  consoleLogs.prepend(entry);
}

function updateCounts() {
  document.getElementById('countAll').textContent = contacts.length;
  document.getElementById('countWork').textContent = contacts.filter(c => c.category === 'Work').length;
  document.getElementById('countPersonal').textContent = contacts.filter(c => c.category === 'Personal').length;
  document.getElementById('countFamily').textContent = contacts.filter(c => c.category === 'Family').length;
}

function renderContacts() {
  const filtered = contacts.filter(c => {
    const matchesCat = activeFilter === 'all' || c.category === activeFilter;
    const matchesSearch = c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          c.email.toLowerCase().includes(searchQuery.toLowerCase()) ||
                          c.phone.includes(searchQuery);
    return matchesCat && matchesSearch;
  });

  updateCounts();

  if (filtered.length === 0) {
    contactsGrid.innerHTML = `
      <div style="grid-column: 1/-1; text-align: center; padding: 60px 20px; color: #94a3b8;">
        <h3>No matching contacts found</h3>
        <p style="margin-top: 8px;">Try modifying your search or filter criteria.</p>
      </div>`;
    return;
  }

  contactsGrid.innerHTML = filtered.map(c => {
    const initials = c.name.split(' ').map(n => n[0]).join('').substring(0, 2).toUpperCase();
    return `
      <div class="contact-card" data-id="${c.id}">
        <div class="card-header-row">
          <div class="card-avatar">${initials}</div>
          <div class="card-name-group">
            <div class="contact-name">${c.name}</div>
            <span class="card-category-badge">${c.category}</span>
          </div>
        </div>
        <div class="contact-info-list">
          <div class="contact-info-item">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg>
            <span>${c.email}</span>
          </div>
          <div class="contact-info-item">
            <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/></svg>
            <span>${c.phone}</span>
          </div>
        </div>
        <div class="card-actions-row">
          <button class="action-icon-btn" onclick="editContact('${c.id}')">Edit</button>
          <button class="action-icon-btn danger" onclick="deleteContact('${c.id}')">Delete</button>
        </div>
      </div>
    `;
  }).join('');
}

// Exception validation matching Java backend
function validateContact(name, email, phone, currentId = null) {
  if (!name || name.trim().length < 2) {
    throw new Error('InvalidContactException: Full name must be at least 2 characters long.');
  }

  const emailRegex = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  if (!emailRegex.test(email.trim())) {
    throw new Error(`InvalidContactException: Malformed email syntax: '${email}'`);
  }

  const phoneRegex = /^\+?[0-9\s-]{7,15}$/;
  if (!phoneRegex.test(phone.trim())) {
    throw new Error(`InvalidContactException: Phone number format invalid: '${phone}'`);
  }

  // Check DuplicateContactException
  const dupEmail = contacts.find(c => c.id !== currentId && c.email.toLowerCase() === email.trim().toLowerCase());
  if (dupEmail) {
    throw new Error(`DuplicateContactException: A contact with email '${email}' already exists.`);
  }

  const dupPhone = contacts.find(c => c.id !== currentId && c.phone === phone.trim());
  if (dupPhone) {
    throw new Error(`DuplicateContactException: A contact with phone '${phone}' already exists.`);
  }
}

// Event Listeners
searchInput.addEventListener('input', (e) => {
  searchQuery = e.target.value;
  renderContacts();
});

document.querySelectorAll('.filter-pill').forEach(btn => {
  btn.addEventListener('click', () => {
    document.querySelectorAll('.filter-pill').forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    activeFilter = btn.dataset.cat;
    renderContacts();
  });
});

document.getElementById('openAddModalBtn').addEventListener('click', () => {
  modalTitle.textContent = 'Create New Contact';
  contactForm.reset();
  document.getElementById('contactId').value = '';
  modalError.classList.add('hidden');
  contactModal.classList.remove('hidden');
});

function closeModal() {
  contactModal.classList.add('hidden');
}

document.getElementById('closeModalBtn').addEventListener('click', closeModal);
document.getElementById('cancelModalBtn').addEventListener('click', closeModal);

contactForm.addEventListener('submit', (e) => {
  e.preventDefault();
  const id = document.getElementById('contactId').value;
  const name = document.getElementById('contactName').value.trim();
  const email = document.getElementById('contactEmail').value.trim();
  const phone = document.getElementById('contactPhone').value.trim();
  const category = document.getElementById('contactCategory').value;

  try {
    validateContact(name, email, phone, id);

    if (id) {
      // Edit
      const existing = contacts.find(c => c.id === id);
      if (!existing) throw new Error(`ContactNotFoundException: Contact ID ${id} not found!`);
      existing.name = name;
      existing.email = email;
      existing.phone = phone;
      existing.category = category;
      logEvent(`Updated contact '${name}' (${id})`, 'success');
    } else {
      // Create
      const newId = 'C-' + Math.floor(100 + Math.random() * 900);
      contacts.push({ id: newId, name, email, phone, category });
      logEvent(`Created new contact '${name}' (${newId})`, 'success');
    }

    closeModal();
    renderContacts();
  } catch (err) {
    modalError.textContent = err.message;
    modalError.classList.remove('hidden');
    logEvent(`THROWN: ${err.message}`, 'error');
  }
});

window.editContact = function(id) {
  const contact = contacts.find(c => c.id === id);
  if (!contact) {
    logEvent(`THROWN: ContactNotFoundException: ID ${id} not found`, 'error');
    return;
  }
  modalTitle.textContent = 'Edit Contact';
  document.getElementById('contactId').value = contact.id;
  document.getElementById('contactName').value = contact.name;
  document.getElementById('contactEmail').value = contact.email;
  document.getElementById('contactPhone').value = contact.phone;
  document.getElementById('contactCategory').value = contact.category;
  modalError.classList.add('hidden');
  contactModal.classList.remove('hidden');
};

window.deleteContact = function(id) {
  const index = contacts.findIndex(c => c.id === id);
  if (index === -1) {
    logEvent(`THROWN: ContactNotFoundException: Failed deleting ${id}`, 'error');
    return;
  }
  const deleted = contacts.splice(index, 1)[0];
  logEvent(`Deleted contact '${deleted.name}' (${id})`, 'info');
  renderContacts();
};

document.getElementById('clearLogBtn').addEventListener('click', () => {
  consoleLogs.innerHTML = '';
});

// CSV Export Simulation
document.getElementById('exportCsvBtn').addEventListener('click', () => {
  let csvContent = 'id,fullName,phoneNumber,emailAddress,category\n';
  contacts.forEach(c => {
    csvContent += `${c.id},${c.name},${c.phone},${c.email},${c.category}\n`;
  });

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.setAttribute('href', url);
  link.setAttribute('download', 'contacts_export.csv');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  logEvent(`[File I/O] Exported ${contacts.length} rows to contacts_export.csv`, 'success');
});

// Initial Render
renderContacts();
