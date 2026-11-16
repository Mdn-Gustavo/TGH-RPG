package com.example.ght.ui.inventario

import androidx.lifecycle.ViewModel
import com.example.ght.data.Inventario

class VMInventario : ViewModel() {
    val itens = Inventario.itens
}
