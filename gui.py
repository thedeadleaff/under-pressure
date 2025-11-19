# sources:
#   https://sengideons.com/tkinter-finance-management-admin-dashboard-gui/
#   https://pythoneo.com/pop-up-windows-in-tkinter/

import random, math, sys, time
from tkinter import *
from tkinter import ttk, font, messagebox
from datetime import datetime, timedelta
from distract import run_tasks

# Saving time of when program opens and terminates after 15 minutes
START = time.time()
TAKE_FIVE = START + 300
TAKE_TEN = TAKE_FIVE + 300
END = START + 900

# Participant and task macros
PARTICIPANT = str(sys.argv[1])
DBD_STATUS = "PENDING"
FITTS_STATUS = "PENDING"
TYPING_STATUS = "PENDING"
auto = False

# Participant information saved
permissions = ['Location', 'History', 'Files and Bookmarks', 'Employee Profile', 'Personal Background']

def minutes():
    return random.randint(1,45)

def completed():
    return random.randint(454,49820)

def autocomplete_status():
    return auto
    
def set_autocomplete_status(status):
    auto = status
class PelioDashboard:
    def __init__(self, root, win_x, win_y):
        self.root = root
        self.win_x = win_x
        self.win_y = win_y
        self.root.title("Under Pressure")
        # self.root.geometry("1680x1050")
        self.root.geometry(f"{self.root.winfo_screenwidth()}x{self.root.winfo_screenheight()}")
        self.root.configure(bg="white")
        
        self.colors = {
            'sidebar': '#2F4F4F',
            'main_bg': '#f8f9fa',
            'card_green': '#10B981',
            'card_blue': '#3B82F6',
            'card_yellow': '#F59E0B',
            'card_red': '#EF4444',
            'card_purple': '#8B5CF6',
            'white': '#ffffff',
            'text_dark': '#1f2937',
            'text_light': '#6b7280',
            'hover': '#f3f4f6'
        }

        self.create_sidebar()
        self.create_main_content()
        
    def create_sidebar(self):
        """Create the left sidebar navigation"""
        self.sidebar = Frame(self.root, bg=self.colors['sidebar'], width=250)
        self.sidebar.pack(side='left', fill='y')
        self.sidebar.pack_propagate(False)

        # Logo and title
        logo_frame = Frame(self.sidebar, bg=self.colors['sidebar'])
        logo_frame.pack(fill='x', pady=20)

        # Pelio logo placeholder
        logo_label = Label(logo_frame, text="☎︎ UNDERPRESSURE", font=('Arial', 18, 'bold'),
                             fg='white', bg=self.colors['sidebar'])
        logo_label.pack(padx=20)
        
        # Navigation items
        nav_items = [
            ("📊 Dashboard", True),
            ("🧾 Invoices", False),
            ("⚙️ Settings", False),
            ("📋 Timetables", False)
        ]
        
        for item, is_active in nav_items:
            bg_color = 'darkslategray' if is_active else self.colors['sidebar']
            fg_color = 'black'
            
            nav_button = Button(self.sidebar, text=item, font=('Arial', 12),
                                  bg=bg_color, fg=fg_color, bd=0, pady=15,
                                  anchor='w', padx=20, cursor='hand2')
            nav_button.pack(fill='x', padx=10, pady=2)
        
        # Bottom section
        bottom_frame = Frame(self.sidebar, bg=self.colors['sidebar'])
        bottom_frame.pack(side='bottom', fill='x', pady=20)
        
        Label(bottom_frame, text="Employee Dashboard", 
                font=('Arial', 10), fg='#95a5a6', bg=self.colors['sidebar']).pack(padx=20)
        Label(bottom_frame, text="© 2025 All rights reserved.", 
                font=('Arial', 9), fg='#7f8c8d', bg=self.colors['sidebar']).pack(padx=20, pady=5)
    
    def create_main_content(self):
        """Create the main content area"""
        self.main_frame = Frame(self.root, bg=self.colors['main_bg'])
        self.main_frame.pack(side='right', fill='both', expand=True)
        
        # Header
        self.create_header()
        
        # Content container with scrollable area
        content_canvas = Canvas(self.main_frame, bg=self.colors['main_bg'], highlightthickness=0)
        # scrollbar = ttk.Scrollbar(self.main_frame, orient="vertical", command=content_canvas.yview)
        self.scrollable_frame = Frame(content_canvas, bg=self.colors['main_bg'])
        
        self.scrollable_frame.bind(
            "<Configure>",
            lambda e: content_canvas.configure(scrollregion=content_canvas.bbox("all"))
        )
        
        content_canvas.create_window((0, 0), window=self.scrollable_frame, anchor="nw")
        # content_canvas.configure(yscrollcommand=scrollbar.set)
        content_canvas.configure()
        
        content_canvas.pack(side="left", fill="both", expand=True)
        # scrollbar.pack(side="right", fill="y")
        
        # Create dashboard sections
        self.create_metric_cards()
        self.create_middle_section()
        self.create_bottom_section()
        
        self.open_popup()
        # self.work_task()
    
    def create_header(self):
        """Create the top header with search and user info"""
        header = Frame(self.main_frame, bg=self.colors['white'], height=80)
        header.pack(fill='x', padx=20, pady=20)
        header.pack_propagate(False)
        
        # Dashboard title
        Label(header, text="📊 Dashboard", font=('Arial', 24, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(side='left', padx=20, pady=20)
        
        # Right side - search and user
        right_frame = Frame(header, bg=self.colors['white'])
        right_frame.pack(side='right', padx=20, pady=20)
        
        # Search box
        # search_frame = Frame(right_frame, bg='#f3f4f6', relief='flat')
        # search_frame.pack(side='left', padx=10)
        
        # search_entry = Entry(search_frame, font=('Arial', 10), bg='#f3f4f6', 
        #                        bd=0, width=20, fg=self.colors['text_light'])
        # search_entry.pack(side='left', padx=10, pady=8)
        # search_entry.insert(0, "Search Here...")
        
        # Notification badges
        notif_frame = Frame(right_frame, bg=self.colors['white'])
        notif_frame.pack(side='left', padx=10)
        
        # Bell icon with badge
        bell_btn = Button(notif_frame, text="🔔", font=('Arial', 16), 
                            bg=self.colors['white'], bd=0, cursor='hand2')
        bell_btn.pack(side='left', padx=5)
        
        # Message icon with badge
        # msg_btn = Button(notif_frame, text="💬", font=('Arial', 16), 
        #                    bg=self.colors['white'], bd=0, cursor='hand2')
        # msg_btn.pack(side='left', padx=5)
        
        # User profile
        user_frame = Frame(right_frame, bg=self.colors['white'])
        user_frame.pack(side='left', padx=10)
        
        # Profile picture placeholder
        profile_btn = Button(user_frame, text="👤", font=('Arial', 16),
                               bg=self.colors['card_red'], fg='white', 
                               width=3, height=1, bd=0, cursor='hand2')
        profile_btn.pack(side='left')
        
        # User info
        user_info = Frame(user_frame, bg=self.colors['white'])
        user_info.pack(side='left', padx=10)
        
        Label(user_info, text="Hi "+PARTICIPANT, font=('Arial', 14, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w')
        Label(user_info, text="Employee Profile", font=('Arial', 12),
                fg=self.colors['text_light'], bg=self.colors['white']).pack(anchor='w')
    
    def create_metric_cards(self):
        """Create the colorful metric cards"""
        cards_frame = Frame(self.scrollable_frame, bg=self.colors['main_bg'])
        cards_frame.pack(fill='x', padx=20, pady=10)
        
        # Card data
        cards_data = [
            (str(completed()), "Completed Invoices", self.colors['card_green'], "📄"),
            (str(completed()), "New Purchases", self.colors['card_yellow'], "💳"),
            (str(completed()), "Completed Timetables", self.colors['card_purple'], "⏳"),
            (str(completed()), "Total Invoices Sent", self.colors['card_red'], "📤")
        ]
        
        for i, (value, label, color, icon) in enumerate(cards_data):
            card = Frame(cards_frame, bg=color, width=280, height=122)
            card.grid(row=0, column=i, padx=10, pady=10, sticky='ew')
            card.pack_propagate(False)
            
            # Icon
            Label(card, text=icon, font=('Arial', 18), 
                    fg='white', bg=color).pack(pady=(15, 5))
            
            # Value
            Label(card, text=value, font=('Arial', 20, 'bold'), 
                    fg='white', bg=color).pack()
            
            # Label
            Label(card, text=label, font=('Arial', 12), 
                    fg='white', bg=color).pack(pady=(0, 10))
        
        # Configure grid weights
        for i in range(4):
            cards_frame.grid_columnconfigure(i, weight=1)
    
    def create_middle_section(self):
        """Create the middle section with transactions and statistics"""
        middle_frame = Frame(self.scrollable_frame, bg=self.colors['main_bg'])
        middle_frame.pack(fill='x', padx=20, pady=20)
        
        # Left side - Latest Transactions
        left_frame = Frame(middle_frame, bg=self.colors['white'], relief='flat', bd=1)
        left_frame.pack(side='right', fill='both', expand=True, padx=(10, 0))
        
        # Right side - To Dos
        right_frame = Frame(middle_frame, bg=self.colors['white'], relief='flat', bd=1)
        right_frame.pack(side='left', fill='both', expand=True, padx=(0, 10))
        
        # Transactions header
        trans_header = Frame(left_frame, bg=self.colors['white'])
        trans_header.pack(fill='x', padx=20, pady=(20, 10))
        
        Label(trans_header, text="Statistics", font=('Arial', 24, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(side='left')
        
        # Transactions list
        self.create_transactions_list(left_frame)
        
        # TODOs header
        stats_header = Frame(right_frame, bg=self.colors['white'])
        stats_header.pack(fill='x', padx=20, pady=(20, 10))
        
        Label(stats_header, text="TO DOs", font=('Arial', 24, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack()
        
        # Create simple bar chart
        self.create_statistics_chart(right_frame)
    
    def create_transactions_list(self, parent):
        """Create the transactions list"""
        chart_frame = Frame(parent, bg=self.colors['white'], height=200)
        chart_frame.pack(fill='x', padx=20, pady=20)
        chart_frame.pack_propagate(False)
        
        # Simple bar chart using canvas
        canvas = Canvas(chart_frame, bg=self.colors['white'], height=150, highlightthickness=0)
        canvas.pack(fill='both', expand=True)
        
        # Draw bars
        bar_colors = [self.colors['card_green'], self.colors['card_red'], 
                     self.colors['card_blue'], self.colors['card_yellow']]
        bar_heights = [80, 60, 100, 45, 70, 85, 55, 90, 65, 75]
        
        bar_width = 25
        spacing = 35
        start_x = 30
        
        for i, height in enumerate(bar_heights):
            x = start_x + i * spacing
            color = bar_colors[i % len(bar_colors)]
            
            # Draw bar
            canvas.create_rectangle(x, 130 - height, x + bar_width, 130, 
                                  fill=color, outline="")
        
        # Legend
        legend_frame = Frame(parent, bg=self.colors['white'])
        legend_frame.pack(fill='x', padx=20, pady=10)
        
        legend_items = [("New Clients", self.colors['card_green']), ("Returning Customers", self.colors['card_blue']),
                       ("Old Clients", self.colors['card_yellow']), ("Clients Lost", self.colors['card_red'])]
        
        for label, color in legend_items:
            item_frame = Frame(legend_frame, bg=self.colors['white'])
            item_frame.pack(side='left', padx=10)
            
            # Color dot
            dot = Frame(item_frame, bg=color, width=10, height=10)
            dot.pack(side='left', padx=(0, 5))
            
            Label(item_frame, text=label, font=('Arial', 9),
                    fg=self.colors['text_light'], bg=self.colors['white']).pack(side='left')
    
    def create_statistics_chart(self, parent):
        """Create a simple statistics chart"""
        transactions_data = [
            ("Answer emails", DBD_STATUS, "Requested "+str(minutes())+" min ago", self.colors['card_green'], "AC"),
            ("Complete projections", FITTS_STATUS, "Requested "+str(minutes())+" min ago", self.colors['card_blue'], "BA"),
            ("Fill timetable", TYPING_STATUS, "Requested "+str(minutes())+" minutes ago", self.colors['card_purple'], "HK")
        ]
        
        for company, amount, time, color, initials in transactions_data:
            trans_row = Frame(parent, bg=self.colors['white'])
            trans_row.pack(fill='x', padx=20, pady=8)
            
            # Company icon
            icon_frame = Frame(trans_row, bg=color, width=40, height=40)
            icon_frame.pack(side='left', padx=(0, 15))
            icon_frame.pack_propagate(False)
            
            Label(icon_frame, text=initials, font=('Arial', 10, 'bold'),
                    fg='white', bg=color).place(relx=0.5, rely=0.5, anchor='center')
            
            # Company details
            details_frame = Frame(trans_row, bg=self.colors['white'])
            details_frame.pack(side='left', fill='x', expand=True)
            
            Label(details_frame, text=company, font=('Arial', 20, 'bold'),
                    fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w')
            Label(details_frame, text=f"#{random.randint(100000, 999999)}-{random.randint(100, 999)}-{random.randint(100, 999)}", 
                    font=('Arial', 12), fg=self.colors['text_light'], bg=self.colors['white']).pack(anchor='w')
            
            # Amount and time
            right_details = Frame(trans_row, bg=self.colors['white'])
            right_details.pack(side='right')
            
            Label(right_details, text=amount, font=('Arial', 16, 'bold'),
                    fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='e')
            Label(right_details, text=time, font=('Arial', 12),
                    fg=self.colors['text_light'], bg=self.colors['white']).pack(anchor='e')
    
    def create_bottom_section(self):
        """Create the bottom section with balance and activity"""
        bottom_frame = Frame(self.scrollable_frame, bg=self.colors['main_bg'])
        bottom_frame.pack(fill='x', padx=20, pady=20)
        
        # Left side - Balance Card
        balance_frame = Frame(bottom_frame, bg=self.colors['card_blue'], width=400, height=200)
        balance_frame.pack(side='left', fill='y', padx=(0, 10))
        balance_frame.pack_propagate(False)
        
        # Balance header
        Label(balance_frame, text="Balance", font=('Arial', 14, 'bold'),
                fg='white', bg=self.colors['card_blue']).pack(anchor='w', padx=20, pady=(20, 5))
        
        # Amount
        Label(balance_frame, text="$ 4,929", font=('Arial', 24, 'bold'),
                fg='white', bg=self.colors['card_blue']).pack(anchor='w', padx=20)
        
        # Card number
        Label(balance_frame, text="•••• •••• •••• 3456", font=('Arial', 12),
                fg='white', bg=self.colors['card_blue']).pack(anchor='w', padx=20, pady=(10, 0))
        
        # Card details
        details_frame = Frame(balance_frame, bg=self.colors['card_blue'])
        details_frame.pack(anchor='w', padx=20, pady=10)
        
        Label(details_frame, text="Valid Thru\n03/28", font=('Arial', 9),
                fg='white', bg=self.colors['card_blue']).pack(side='left')
        
        Label(details_frame, text=f"Name\n{PARTICIPANT}", font=('Arial', 9),
                fg='white', bg=self.colors['card_blue']).pack(side='left', padx=(30, 0))
        
        # Middle section - Balance Details
        middle_frame = Frame(bottom_frame, bg=self.colors['white'])
        middle_frame.pack(side='left', fill='both', expand=True, padx=10)
        
        # Balance Details header
        Label(middle_frame, text="Balance Details", font=('Arial', 16, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w', padx=20, pady=(20, 10))
        
        # Total Balance
        Label(middle_frame, text="Total Balance", font=('Arial', 10),
                fg=self.colors['text_light'], bg=self.colors['white']).pack(anchor='w', padx=20)
        Label(middle_frame, text="$221,478", font=('Arial', 20, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w', padx=20, pady=(0, 10))
        
        # Monthly stats
        stats_row = Frame(middle_frame, bg=self.colors['white'])
        stats_row.pack(fill='x', padx=20, pady=10)
        
        # Last Month
        left_stat = Frame(stats_row, bg=self.colors['card_blue'], width=120, height=50)
        left_stat.pack(side='left', padx=(0, 10))
        left_stat.pack_propagate(False)
        
        Label(left_stat, text="Last Month", font=('Arial', 9),
                fg='white', bg=self.colors['card_blue']).pack(pady=(8, 0))
        Label(left_stat, text="$ 16,849", font=('Arial', 11, 'bold'),
                fg='white', bg=self.colors['card_blue']).pack()
        
        # Expenses
        Label(stats_row, text="Expenses\n$ 13,849", font=('Arial', 10),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(side='left', padx=20)
        
        # Other stats
        other_stats = Frame(middle_frame, bg=self.colors['white'])
        other_stats.pack(fill='x', padx=20, pady=10)
        
        Label(other_stats, text=f"Income Generated     ${completed()}", font=('Arial', 10),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w')
        Label(other_stats, text="Company Total       $365,478", font=('Arial', 10),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(anchor='w', pady=(5, 0))
        
        # Right side - Activity
        activity_frame = Frame(bottom_frame, bg=self.colors['white'], width=250)
        activity_frame.pack(side='right', fill='y', padx=(10, 0))
        activity_frame.pack_propagate(False)
        
        # Activity header
        activity_header = Frame(activity_frame, bg=self.colors['white'])
        activity_header.pack(fill='x', padx=20, pady=(20, 10))
        
        Label(activity_header, text="Activity", font=('Arial', 14, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(side='left')
        Label(activity_header, text="$ 48,284", font=('Arial', 14, 'bold'),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack(side='right')
        
        # Activity stats
        stats_frame = Frame(activity_frame, bg=self.colors['white'])
        stats_frame.pack(fill='x', padx=20, pady=10)
        
        Label(stats_frame, text="267    312    176", font=('Arial', 12),
                fg=self.colors['text_dark'], bg=self.colors['white']).pack()
        
        # Activity chart (simplified)
        chart_canvas = Canvas(activity_frame, bg=self.colors['white'], height=100, highlightthickness=0)
        chart_canvas.pack(fill='x', padx=20, pady=10)
        
        # Draw simple bars
        colors = [self.colors['card_green'], self.colors['card_red'], self.colors['card_blue']]
        heights = [60, 40, 70]
        
        for i, (height, color) in enumerate(zip(heights, colors)):
            x = 30 + i * 60
            chart_canvas.create_rectangle(x, 80 - height, x + 40, 80, fill=color, outline="")
        
        # Month labels
        months = ["Sep", "Mar", "Apr"]
        for i, month in enumerate(months):
            x = 50 + i * 60
            chart_canvas.create_text(x, 90, text=month, font=('Arial', 9), 
                                   fill=self.colors['text_light'])
        
        # Activity list
        activities = [
            "Revenue Generated"
        ]
        
        for activity in activities:
            activity_row = Frame(activity_frame, bg=self.colors['white'])
            activity_row.pack(fill='x', padx=20, pady=2)
            
            # Color dot
            dot = Frame(activity_row, bg=self.colors['card_green'], width=8, height=8)
            dot.pack(side='left', padx=(0, 10))
            
            Label(activity_row, text=activity, font=('Arial', 9),
                    fg=self.colors['text_light'], bg=self.colors['white']).pack(side='left')
    
    def populate_data(self):
        """Populate dashboard with sample data"""
        pass  # Data is already populated in the creation methods

    def open_popup(self):
        popup = Toplevel(background="darkslategray")
        popup.attributes('--topmost', True)
        popup.title("A New Message from The Office")
        popup.geometry(f"600x200+{int(self.root.winfo_screenwidth()/2-250)}+{int(self.root.winfo_screenheight()/4)}")
        popup.focus_force
        label = Label(popup, background="darkslategray", foreground="white", text="Welcome back!\n\n"+
                      "Your dashboard offers a summary of organization progress and any new work assignments.\n"+
                      "You have new work assignments to complete!")
        label.pack(padx=20, pady=20)
        button = Button(popup, text="See first work task", background="white", foreground=self.colors['text_dark'],
                        command=lambda: [popup.destroy(), self.work()])
        button.pack(pady=10)
    
    def work(self):
        result = messagebox.askquestion("New Work Task Assigned!", "You have been assigned a new work task to complete.\n"+
                                        "\nAutocompleting will access your "+random.choice(permissions)+".\n\nWould you like to autocomplete this task?"
                                        , icon=messagebox.WARNING)
        if result == 'yes':
            run_tasks(PARTICIPANT, END, True, permissions)
        else:
            run_tasks(PARTICIPANT, END, False, permissions)

def main():
    root = Tk()
    win_x = root.winfo_rootx()
    win_y = root.winfo_rooty()
    app = PelioDashboard(root, win_x, win_y)
    # if time.time() == TAKE_FIVE:
        # app.work_task()
        # autocomplete_status
    # root.mainloop()
    if time.time() >= END:
        messagebox.showinfo("Clocked Out", "Your work day is finally finished. Goodbye!")
        # Give enough time for the user to read the exit message
        time.sleep(5)
        sys.exit()
    else:
        app.root.mainloop()

if __name__ == "__main__":
    main()
