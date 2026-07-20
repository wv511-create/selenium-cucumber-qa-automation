// Running in local demo mode — no backend required.
// Firebase was scaffolded but never wired into the rendering logic below,
// so the app runs entirely off state.machines / state.bookings.

// Application State
const state = {
    user: null, // { name, phone, village, role }
    machines: [
        { id: 1, name: "Mahindra 575 DI Tractor", owner: "Ramesh Kumar", phone: "+91 98765 43210", hourly: 500, daily: 3500, status: "Available", rating: 4, distance: "2.3km", type: "Tractor" },
        { id: 2, name: "Sonalika Harvester", owner: "Suresh Patil", phone: "+91 91234 56789", hourly: 800, daily: 5500, status: "Booked", rating: 5, distance: "4.1km", type: "Harvester" },
        { id: 3, name: "VST Power Tiller", owner: "Lakshmi Devi", phone: "+91 87654 32109", hourly: 300, daily: 2000, status: "Available", rating: 3, distance: "1.8km", type: "Power Tiller" },
        { id: 4, name: "Honda Sprayer", owner: "Anil Gowda", phone: "+91 76543 21098", hourly: 200, daily: 1200, status: "Available", rating: 4, distance: "3.5km", type: "Sprayer" },
        { id: 5, name: "John Deere 5050D Tractor", owner: "Venkat Reddy", phone: "+91 65432 10987", hourly: 600, daily: 4200, status: "Available", rating: 5, distance: "5.2km", type: "Tractor" },
        { id: 6, name: "Kubota Harvester", owner: "Meena Bai", phone: "+91 54321 09876", hourly: 750, daily: 5000, status: "Booked", rating: 3, distance: "6.0km", type: "Harvester" }
    ],
    bookings: [
        { id: 101, farmer: "Kiran Kumar", phone: "+91 94321 11111", machine: "Mahindra Tractor", date: "15 May", duration: "4hrs", cost: 2000, status: "Pending", ownerPhone: "+91 98765 43210", purpose: "Ploughing" },
        { id: 102, farmer: "Priya Nair", phone: "+91 83210 22222", machine: "VST Power Tiller", date: "16 May", duration: "1 day", cost: 2000, status: "Pending", ownerPhone: "+91 87654 32109", purpose: "Field prep" },
        { id: 103, farmer: "Mohan Das", phone: "+91 72109 33333", machine: "Honda Sprayer", date: "17 May", duration: "3hrs", cost: 600, status: "Pending", ownerPhone: "+91 76543 21098", purpose: "Pesticide spray" }
    ],
    currentScreen: 'role_selection'
};

const content = document.getElementById('content-area');
const nav = document.getElementById('bottom-nav');

function navigate(screen, params = {}) {
    state.currentScreen = screen;
    render(params);
}

function render(params = {}) {
    content.innerHTML = '';
    nav.classList.add('hidden');

    switch(state.currentScreen) {
        case 'role_selection':
            renderRoleSelection();
            break;
        case 'farmer_browse':
            renderFarmerBrowse();
            break;
        case 'booking':
            renderBooking(params.id);
            break;
        case 'my_bookings':
            renderMyBookings();
            break;
        case 'price_calculator':
            renderPriceCalculator();
            break;
        case 'owner_dashboard':
            renderOwnerDashboard();
            break;
        case 'owner_machines':
            renderOwnerMachines();
            break;
        case 'owner_requests':
            renderOwnerRequests();
            break;
        case 'profile':
            renderProfile();
            break;
    }
}

function renderRoleSelection() {
    content.innerHTML = `
        <div class="role-selection">
            <h1>Namma-Yantra Share</h1>
            <p style="margin-bottom: 24px">Choose your role to continue</p>
            <div class="role-cards">
                <div class="role-card" onclick="setRole('Farmer')">
                    <i class="fas fa-tractor" style="font-size: 32px; color: var(--primary)"></i>
                    <div style="font-weight: bold; margin-top: 8px">Farmer</div>
                </div>
                <div class="role-card" onclick="setRole('Owner')">
                    <i class="fas fa-user-tie" style="font-size: 32px; color: var(--primary)"></i>
                    <div style="font-weight: bold; margin-top: 8px">Owner</div>
                </div>
            </div>
            <input type="text" id="reg-name" placeholder="Full Name">
            <input type="text" id="reg-phone" placeholder="Phone Number">
            <input type="text" id="reg-village" placeholder="Village">
            <p id="reg-error" style="color: red; font-size: 12px; margin-top: 8px; display: none">Please fill all fields</p>
        </div>
    `;
}

window.setRole = (role) => {
    const name = document.getElementById('reg-name').value;
    const phone = document.getElementById('reg-phone').value;
    const village = document.getElementById('reg-village').value;

    if (!name || !phone || !village) {
        document.getElementById('reg-error').style.display = 'block';
        return;
    }

    state.user = { name, phone, village, role };
    navigate(role === 'Farmer' ? 'farmer_browse' : 'owner_dashboard');
};

function renderFarmerBrowse() {
    nav.classList.remove('hidden');
    renderFarmerNav(0);
    content.innerHTML = `
        <div style="padding: 16px">
            <input type="text" placeholder="Search machines..." style="border-radius: 24px">
            <div class="filter-chips">
                <div class="chip active">All</div>
                <div class="chip">Tractor</div>
                <div class="chip">Harvester</div>
                <div class="chip">Power Tiller</div>
                <div class="chip">Sprayer</div>
            </div>
            ${state.machines.map(m => `
                <div class="card" onclick="navigate('booking', {id: ${m.id}})">
                    <span class="badge ${m.status === 'Available' ? 'badge-available' : 'badge-booked'}">${m.status}</span>
                    <div style="font-weight: bold; font-size: 18px">${m.name}</div>
                    <div style="font-size: 14px; color: var(--text-secondary)">Owner: ${m.owner}</div>
                    <div style="font-size: 14px; margin-top: 4px">
                        <i class="fas fa-star" style="color: var(--accent)"></i> ${m.rating} | ${m.distance}
                    </div>
                    <div style="display: flex; justify-content: space-between; align-items: flex-end; margin-top: 12px">
                        <div>
                            <div style="font-weight: bold">₹${m.hourly}/hr</div>
                            <div style="font-size: 12px; color: var(--text-secondary)">₹${m.daily}/day</div>
                        </div>
                        <button class="btn" style="width: auto; padding: 8px 16px" ${m.status === 'Booked' ? 'disabled' : ''}>Book Now</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderBooking(id) {
    const machine = state.machines.find(m => m.id === id);
    content.innerHTML = `
        <div style="padding: 24px">
            <div style="display: flex; align-items: center; margin-bottom: 24px">
                <i class="fas fa-arrow-left" onclick="navigate('farmer_browse')" style="font-size: 20px; margin-right: 16px"></i>
                <h2 style="margin: 0">Book ${machine.name}</h2>
            </div>
            
            <div class="card">
                <div style="font-weight: bold">${machine.owner}</div>
                <div style="color: var(--primary); margin: 8px 0">${machine.phone}</div>
                <div style="display: flex; gap: 8px">
                    <button class="btn btn-outline" style="flex: 1"><i class="fas fa-phone"></i> Call</button>
                    <button class="btn btn-outline" style="flex: 1; border-color: #25D366; color: #25D366"><i class="fab fa-whatsapp"></i> WhatsApp</button>
                </div>
            </div>

            <div style="margin-top: 24px">
                <label style="font-size: 14px; font-weight: bold">Farmer Details</label>
                <div style="background: #eee; padding: 12px; border-radius: 8px; margin-top: 4px">
                    <div>${state.user.name}</div>
                    <div style="font-size: 14px; color: var(--text-secondary)">${state.user.phone}</div>
                </div>
            </div>

            <div style="margin-top: 24px">
                <input type="date" value="2026-05-15">
                <div style="display: flex; justify-content: space-between; align-items: center; margin: 16px 0">
                    <span>Duration</span>
                    <div style="display: flex; align-items: center; gap: 12px">
                        <i class="fas fa-minus-circle" style="font-size: 24px; color: var(--primary)"></i>
                        <span style="font-weight: bold; font-size: 18px">4 hrs</span>
                        <i class="fas fa-plus-circle" style="font-size: 24px; color: var(--primary)"></i>
                    </div>
                </div>
            </div>

            <div class="card" style="background: var(--primary-container); margin-top: 40px">
                <div style="display: flex; justify-content: space-between; align-items: center">
                    <span>Total Cost</span>
                    <span style="font-size: 24px; font-weight: bold; color: var(--primary)">₹2,000</span>
                </div>
            </div>

            <button class="btn" style="margin-top: 16px" onclick="confirmBooking('${machine.name}', 2000)">Confirm Booking</button>
        </div>
    `;
}

window.confirmBooking = (name, cost) => {
    state.bookings.unshift({
        id: Date.now(),
        farmer: state.user.name,
        phone: state.user.phone,
        machine: name,
        date: "15 May",
        duration: "4hrs",
        cost: cost,
        status: "Pending",
        ownerPhone: "+91 98765 43210"
    });
    alert("Booking Successful! Owner notified.");
    navigate('my_bookings');
};

function renderMyBookings() {
    nav.classList.remove('hidden');
    renderFarmerNav(1);
    content.innerHTML = `
        <div style="padding: 16px">
            <h1>My Bookings</h1>
            <div style="display: flex; border-bottom: 1px solid #ddd; margin-bottom: 16px">
                <div style="flex: 1; text-align: center; padding: 12px; border-bottom: 2px solid var(--primary); font-weight: bold">Pending</div>
                <div style="flex: 1; text-align: center; padding: 12px; color: #888">Accepted</div>
                <div style="flex: 1; text-align: center; padding: 12px; color: #888">Declined</div>
            </div>
            ${state.bookings.filter(b => b.phone === state.user.phone).map(b => `
                <div class="card">
                    <div style="display: flex; justify-content: space-between">
                        <div style="font-weight: bold">${b.machine}</div>
                        <div style="color: var(--accent); font-weight: bold">${b.status}</div>
                    </div>
                    <div style="font-size: 14px; margin: 4px 0">Owner: ${b.ownerPhone}</div>
                    <div style="font-size: 14px; color: var(--text-secondary)">${b.date} | ${b.duration}</div>
                    <div style="font-weight: bold; margin-top: 8px">Cost: ₹${b.cost}</div>
                    <div style="display: flex; gap: 8px; margin-top: 12px">
                        <button class="btn btn-outline" style="flex: 1">Call Owner</button>
                        ${b.status === 'Pending' ? `<button class="btn btn-outline" style="flex: 1; color: var(--error); border-color: var(--error)">Cancel</button>` : ''}
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderPriceCalculator() {
    nav.classList.remove('hidden');
    renderFarmerNav(2);
    content.innerHTML = `
        <div style="padding: 16px">
            <h1>Price Calculator</h1>
            <label style="font-size: 14px; font-weight: bold">Machine Type</label>
            <select style="width: 100%; padding: 12px; margin: 8px 0; border-radius: 8px; border: 1px solid #ccc">
                <option>Tractor (₹500/hr)</option>
                <option>Harvester (₹800/hr)</option>
                <option>Power Tiller (₹300/hr)</option>
                <option>Sprayer (₹200/hr)</option>
            </select>
            
            <div style="display: flex; align-items: center; gap: 16px; margin: 24px 0">
                <span style="font-weight: bold">Hourly</span>
                <div style="width: 40px; height: 20px; background: var(--primary); border-radius: 10px; position: relative">
                    <div style="width: 16px; height: 16px; background: white; border-radius: 50%; position: absolute; left: 2px; top: 2px"></div>
                </div>
                <span style="color: #888">Daily</span>
            </div>

            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px">
                <span>Duration</span>
                <div style="display: flex; align-items: center; gap: 12px">
                    <i class="fas fa-minus-circle" style="font-size: 32px; color: var(--primary)"></i>
                    <span style="font-weight: bold; font-size: 24px">5 hrs</span>
                    <i class="fas fa-plus-circle" style="font-size: 32px; color: var(--primary)"></i>
                </div>
            </div>

            <div class="card" style="background: var(--primary); color: white; text-align: center; padding: 32px">
                <div style="font-size: 14px; opacity: 0.8">Estimated Total</div>
                <div style="font-size: 48px; font-weight: bold">₹2,500</div>
            </div>
        </div>
    `;
}

function renderOwnerDashboard() {
    nav.classList.remove('hidden');
    renderOwnerNav(0);
    content.innerHTML = `
        <div style="padding: 16px">
            <h1>Owner Dashboard</h1>
            <div class="stat-grid">
                <div class="stat-card">
                    <div class="stat-val">4</div>
                    <div class="stat-label">Machines</div>
                </div>
                <div class="stat-card">
                    <div class="stat-val">3</div>
                    <div class="stat-label">Pending</div>
                </div>
                <div class="stat-card">
                    <div class="stat-val">₹12.4k</div>
                    <div class="stat-label">Earned</div>
                </div>
            </div>

            <h2>Recent Requests</h2>
            ${state.bookings.slice(0, 3).map(b => `
                <div class="card">
                    <div style="display: flex; justify-content: space-between">
                        <div style="font-weight: bold">${b.farmer}</div>
                        <div style="color: var(--primary); font-size: 14px">${b.machine}</div>
                    </div>
                    <div style="font-size: 14px; color: var(--text-secondary)">${b.date} | ${b.duration}</div>
                    <div style="font-weight: bold; margin-top: 8px">₹${b.cost}</div>
                    <div style="display: flex; gap: 8px; margin-top: 12px">
                        <button class="btn" style="flex: 1" onclick="updateStatus(${b.id}, 'Accepted')">Accept</button>
                        <button class="btn btn-outline" style="flex: 1" onclick="updateStatus(${b.id}, 'Declined')">Decline</button>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

window.updateStatus = (id, status) => {
    const booking = state.bookings.find(b => b.id === id);
    if (booking) booking.status = status;
    render();
};

function renderOwnerMachines() {
    nav.classList.remove('hidden');
    renderOwnerNav(1);
    content.innerHTML = `
        <div style="padding: 16px">
            <div style="display: flex; justify-content: space-between; align-items: center">
                <h1>My Machines</h1>
                <button class="btn" style="width: auto; padding: 8px 16px"><i class="fas fa-plus"></i> Add</button>
            </div>
            ${state.machines.slice(0,3).map(m => `
                <div class="card">
                    <div style="display: flex; justify-content: space-between; align-items: center">
                        <div style="font-weight: bold">${m.name}</div>
                        <div style="width: 40px; height: 20px; background: var(--primary); border-radius: 10px; position: relative">
                            <div style="width: 16px; height: 16px; background: white; border-radius: 50%; position: absolute; right: 2px; top: 2px"></div>
                        </div>
                    </div>
                    <div style="font-size: 14px; color: var(--text-secondary); margin-top: 4px">Rate: ₹${m.hourly}/hr | ₹${m.daily}/day</div>
                    <div style="display: flex; gap: 16px; margin-top: 12px; justify-content: flex-end">
                        <i class="fas fa-edit" style="color: var(--primary)"></i>
                        <i class="fas fa-trash" style="color: var(--error)"></i>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}

function renderOwnerRequests() {
    nav.classList.remove('hidden');
    renderOwnerNav(2);
    content.innerHTML = `
        <div style="padding: 16px">
            <h1>All Requests</h1>
            ${state.bookings.map(b => `
                <div class="card">
                    <div style="display: flex; justify-content: space-between">
                        <div style="font-weight: bold">${b.farmer}</div>
                        <div style="font-size: 14px; color: ${b.status === 'Accepted' ? 'green' : (b.status === 'Declined' ? 'red' : 'orange')}">${b.status}</div>
                    </div>
                    <div style="color: var(--primary); margin: 4px 0">${b.phone}</div>
                    <div style="font-size: 14px">${b.machine} - ${b.purpose}</div>
                    <div style="font-size: 14px; color: var(--text-secondary)">${b.date} | ${b.duration} | ₹${b.cost}</div>
                    ${b.status === 'Pending' ? `
                        <div style="display: flex; gap: 8px; margin-top: 12px">
                            <button class="btn" style="flex: 1" onclick="updateStatus(${b.id}, 'Accepted')">Accept</button>
                            <button class="btn btn-outline" style="flex: 1" onclick="updateStatus(${b.id}, 'Declined')">Decline</button>
                        </div>
                    ` : `
                        <button class="btn btn-outline" style="margin-top: 12px">Call Farmer</button>
                    `}
                </div>
            `).join('')}
        </div>
    `;
}

function renderProfile() {
    nav.classList.remove('hidden');
    if (state.user.role === 'Farmer') renderFarmerNav(3);
    else renderOwnerNav(3);

    content.innerHTML = `
        <div style="padding: 32px; text-align: center">
            <div style="width: 100px; height: 100px; background: var(--primary-container); border-radius: 50%; margin: 0 auto 16px; display: flex; justify-content: center; align-items: center">
                <i class="fas fa-user" style="font-size: 40px; color: var(--primary)"></i>
            </div>
            <h1>${state.user.name}</h1>
            <div style="color: var(--text-secondary)">${state.user.phone}</div>
            <div style="color: var(--text-secondary)">${state.user.village}</div>
            <div style="margin-top: 8px; color: var(--primary); font-weight: bold">${state.user.role}</div>
            
            <button class="btn" style="margin-top: 60px; background: var(--error)" onclick="logout()">Logout</button>
        </div>
    `;
}

window.logout = () => {
    state.user = null;
    navigate('role_selection');
};

function renderFarmerNav(activeIdx) {
    const items = [
        { icon: 'fa-search', label: 'Browse', screen: 'farmer_browse' },
        { icon: 'fa-list-ul', label: 'Bookings', screen: 'my_bookings' },
        { icon: 'fa-calculator', label: 'Price', screen: 'price_calculator' },
        { icon: 'fa-user', label: 'Profile', screen: 'profile' }
    ];
    nav.innerHTML = items.map((item, i) => `
        <div class="nav-item ${i === activeIdx ? 'active' : ''}" onclick="navigate('${item.screen}')">
            <i class="fas ${item.icon}"></i>
            <span>${item.label}</span>
        </div>
    `).join('');
}

function renderOwnerNav(activeIdx) {
    const items = [
        { icon: 'fa-chart-pie', label: 'Home', screen: 'owner_dashboard' },
        { icon: 'fa-tractor', label: 'Machines', screen: 'owner_machines' },
        { icon: 'fa-bell', label: 'Requests', screen: 'owner_requests' },
        { icon: 'fa-user', label: 'Profile', screen: 'profile' }
    ];
    nav.innerHTML = items.map((item, i) => `
        <div class="nav-item ${i === activeIdx ? 'active' : ''}" onclick="navigate('${item.screen}')">
            <i class="fas ${item.icon}"></i>
            <span>${item.label}</span>
        </div>
    `).join('');
}

// Start app
render();
