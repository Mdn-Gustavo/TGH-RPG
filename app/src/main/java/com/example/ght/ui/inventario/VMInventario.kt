package com.example.ght.ui.inventario

import androidx.lifecycle.ViewModel
import com.example.ght.data.Inventario

class VMInventario : ViewModel() {
    // A tela só lê a lista que está guardada em Inventario
    val itens = Inventario.itens
}
